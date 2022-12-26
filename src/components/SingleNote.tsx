import {Appbar, Button, Text, TextInput} from 'react-native-paper';
import React from 'react';
import {NoteInterface} from '../entity/NoteInterface';
import {View} from 'react-native';
import {Controller, useForm} from 'react-hook-form';
import {postNote} from '../util/handleAuthentication';

const SingleNote: React.FC<{
  data: NoteInterface;
  onDiscard: () => void;
}> = props => {
  const data = props.data;
  const isNewNote = data.id === undefined;
  let {control, handleSubmit} = useForm<NoteInterface>({
    mode: 'onChange',
    defaultValues: data,
  });
  return (
    <View>
      <Appbar.Header>
        <Appbar.BackAction onPress={() => props.onDiscard()} />
        <Appbar.Content title="Title" />
      </Appbar.Header>
      <View
        style={{
          width: '95%',
          alignSelf: 'center',
        }}
        nativeID="content-id">
        <View style={{marginVertical: 10}}>
          <Controller
            control={control}
            render={({field: {onChange, onBlur, value}}) => (
              <TextInput
                label={'Content'}
                multiline={true}
                numberOfLines={15}
                onBlur={onBlur}
                onChangeText={onChange}
                value={value}
              />
            )}
            name={'content'}
          />
        </View>
        <View style={{marginVertical: 10}}>
          <Controller
            control={control}
            render={({field: {onChange, onBlur, value}}) => (
              <TextInput
                label={'Title'}
                onBlur={onBlur}
                onChangeText={onChange}
                value={value}
              />
            )}
            name={'title'}
          />
        </View>
      </View>
      {!isNewNote && (
        <View>
          <Text>Last edited at {data.lastEdited}</Text>
        </View>
      )}
      <View
        style={{
          display: 'flex',
          flexDirection: 'row',
          alignSelf: 'center',
          marginTop: 'auto',
          marginBottom: 0,
        }}>
        <Button
          onPress={handleSubmit(postNote, console.log)}
          mode={'outlined'}
          style={{marginHorizontal: 10}}>
          Save
        </Button>
        <Button onPress={handleSubmit(postNote, console.log)} mode={'outlined'}>
          Delete
        </Button>
      </View>
    </View>
  );
};
export default SingleNote;
