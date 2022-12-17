import React from 'react';
import {getNotes, handleLogout} from '../util/handleAuthentication';
import {Button} from 'react-native-paper';

const ProfileScreen: React.FC = () => {
  return (
    <>
      <Button
        style={{alignSelf: 'center', marginBottom: 0, marginTop: 'auto'}}
        mode={'outlined'}
        onPress={() => handleLogout()}>
        Sign out
      </Button>
      <Button
        style={{alignSelf: 'center', marginBottom: 0, marginTop: 'auto'}}
        mode={'outlined'}
        onPress={() => getNotes()}>
        Test
      </Button>
    </>
  );
};

export default ProfileScreen;
