import React, {useEffect, useState} from 'react';
import {ScrollView, View} from 'react-native';
import NoteView from '../components/NoteView';
import {Button, Modal, Searchbar, Text} from 'react-native-paper';
import {NoteInterface} from '../entity/NoteInterface';
import {launchImageLibrary} from 'react-native-image-picker';
import {url} from '../util/handleAuthentication';
import {useNotes} from '../util/useNotes';
import SingleNote from '../components/SingleNote';
import {useMutation} from 'react-query';
import axios from 'axios';
import {FailResponseEntity} from '../entity/FailResponseEntity';

function filterFunction(el: NoteInterface, query: string): boolean {
  if (!el.content) {
    return false;
  }
  if (query.trim() === '') {
    return true;
  }
  return el.content.startsWith(query) || el.content.startsWith(query);
}

function render(data: NoteInterface[], onClick: (data: NoteInterface) => void) {
  return data.map((value, index) => (
    <View key={index} style={{marginBottom: 5}}>
      <NoteView data={value} onClick={() => onClick(value)} />
    </View>
  ));
}

function extracted(content: string) {
  return {
    id: undefined,
    content: content,
    date: undefined,
    category: undefined,
    lastEdited: undefined,
    title: undefined,
  };
}

interface ImageRequest {
  uri: string | undefined;
  name: string | undefined;
  type: string | undefined;
}

const NoteScreen: React.FC = () => {
  const [searchContent, setSearchContent] = useState<string>('');
  const [selectedNote, setSelectedNote] = useState<NoteInterface | undefined>(
    undefined,
  );
  const {data, refetch} = useNotes();
  const mutation = useMutation<string, FailResponseEntity, ImageRequest>({
    mutationFn: variables => {
      const formData = new FormData();
      formData.append('file', variables);
      return axios
        .post(`${url}/api/images`, formData, {
          headers: {'Content-Type': 'multipart/form-data'},
        })
        .then(value => value.data);
    },
  });
  useEffect(() => {
    if (mutation.data) {
      refetch();
    }
    if (mutation.data !== undefined) {
      const newVar = extracted(mutation.data);
      setSelectedNote(newVar);
    }
  }, [mutation.data]);

  return (
    <React.Fragment>
      {selectedNote && (
        <SingleNote
          onDiscard={() => {
            refetch();
            setSelectedNote(undefined);
          }}
          data={selectedNote}
        />
      )}
      {!selectedNote && (
        <>
          <Searchbar
            placeholder="Search"
            value={searchContent}
            onChangeText={setSearchContent}
          />
          <ScrollView style={{marginTop: 20, marginHorizontal: 10}}>
            {data &&
              render(
                data.filter(el => filterFunction(el, searchContent)),
                setSelectedNote,
              )}
          </ScrollView>
          <Button
            style={{alignSelf: 'center', marginBottom: 0, marginTop: 'auto'}}
            mode={'outlined'}
            onPress={async () => {
              const result = await launchImageLibrary({
                mediaType: 'photo',
              });
              if (!result.assets) {
                return;
              }
              let asset = result.assets[0];
              try {
                const imageRequest: ImageRequest = {
                  uri: asset.uri,
                  name: asset.fileName,
                  type: asset.type,
                };
                mutation.mutate(imageRequest);
              } catch (e) {
                console.log(e);
              }
            }}>
            Library
          </Button>
        </>
      )}
      <Modal
        visible={mutation.isLoading}
        dismissable={true}
        contentContainerStyle={{backgroundColor: 'white', padding: 20}}>
        <Text>Processing image</Text>
      </Modal>
    </React.Fragment>
  );
};
export default NoteScreen;
