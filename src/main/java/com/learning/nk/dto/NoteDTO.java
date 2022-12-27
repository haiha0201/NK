package com.learning.nk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.learning.nk.model.CustomUser;
import com.learning.nk.model.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoteDTO {
    private String content;
    private String title;

    private Date date = new Date();
    private Date lastEdited = new Date();

    private String category;
    private Long id;

    public static NoteDTO fromNote(Note note) {
        return NoteDTO.builder()
                .content(note.getContent())
                .title(note.getTitle())
                .category(note.getCategory())
                .id(note.getId())
                .build();
    }

    public Note toNote(CustomUser customUser) {
        return Note.builder()
                .content(this.content)
                .user(customUser)
                .title(this.title)
                .category(category)
                .date(date)
                .lastModified(lastEdited)
                .build();
    }
}
