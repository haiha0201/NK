package com.learning.nk.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.learning.nk.dto.NoteDTO;
import com.learning.nk.exception.InvalidRequestException;
import com.learning.nk.exception.UnsupportedAuthenticationException;
import com.learning.nk.model.CustomUser;
import com.learning.nk.model.Note;
import com.learning.nk.repository.NoteRepo;
import com.learning.nk.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Service
public class NoteService implements NoteServiceInterface {
    @Value("${detect.endpoint}")
    public String URI;
    final NoteRepo noteRepo;
    final ElasticsearchClient esClient;
    final UserRepo userRepo;
    Logger logger = LoggerFactory.getLogger(NoteService.class);

    @Autowired
    public NoteService(NoteRepo noteRepo , ElasticsearchClient esClient , UserRepo userRepo) {
        this.noteRepo = noteRepo;
        this.esClient = esClient;
        this.userRepo = userRepo;
    }

    public String parseContent(String imageURI) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        logger.info(imageURI);
        String urlTemplate = String.format("%s?url=%s" , URI , imageURI);
        logger.info(urlTemplate);


        ResponseEntity<String> result =
                restTemplate.exchange(urlTemplate , HttpMethod.GET , entity , String.class);
        return result.getBody();
    }

    @Override
    public void put(NoteDTO noteDTO) {
        validate(noteDTO);
        noteDTO.setLastEdited(new Date());
        save(noteDTO);
    }

    private void validate(NoteDTO noteDTO) {
        if (noteDTO.getId() == null || !noteRepo.existsById(noteDTO.getId())) {
            throw new InvalidRequestException("Note by id not found!");
        }
    }

    @Override
    public void upload(NoteDTO noteDTO) {
        save(noteDTO);
    }

    @Override
    public void delete(Long id) {
        noteRepo.deleteById(id);
        try {
            esClient.deleteByQuery(builder -> builder.index("note")
                    .query(q -> q.term(t -> t.field("id")
                            .value(id))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<NoteDTO> suggest(String query) {
        try {
            SearchResponse<Note> response = esClient.search(s -> s.index("note")
                    .query(q -> q.matchPhrasePrefix(t -> t.field("content")
                            .slop(4)
                            .query(query))) , Note.class);
            List<Hit<Note>> hits = response.hits()
                    .hits();
            List<NoteDTO> collect = hits.stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .map(note -> noteRepo.findById(note.getId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(NoteDTO::fromNote)
                    .collect(Collectors.toList());
            return collect;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Reader serialize(Note entity) {
        return new StringReader(
                (String.format("{'content': '%s', 'title': '%s', 'category': '%s', 'id' : '%d'}" ,
                        entity.getContent() , entity.getTitle() , entity.getCategory() ,
                        entity.getId())).replace('\'' , '"'));
    }

    void save(NoteDTO noteDTO) {
        CustomUser userDetails = getUserDetails();
        Note entity = noteDTO.toNote(userDetails);
        Note save = noteRepo.save(entity);
        try {
            Reader serialize = serialize(save);
            IndexRequest<Note> request = IndexRequest.of(i -> i.index("note")
                    .withJson(serialize));
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<NoteDTO> getAll() {
        CustomUser userDetails = getUserDetails();
        return userDetails.getNotes()
                .stream()
                .map(NoteDTO::fromNote)
                .collect(Collectors.toList());
    }

    public CustomUser getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Class<?> clazz = authentication.getPrincipal()
                .getClass();
        Class<CustomUser> customUserClass = CustomUser.class;
        if (clazz.equals(customUserClass)) {
            String name = authentication.getName();
            return userRepo.getByUsername(name);
        }
        throw new UnsupportedAuthenticationException();
    }
}
