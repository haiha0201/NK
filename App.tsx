/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, {useEffect, useState} from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createMaterialBottomTabNavigator} from '@react-navigation/material-bottom-tabs';
import NoteScreen from './src/screens/NoteScreen';
import CanvasScreen from './src/screens/CanvasScreen';
import {useAppSelector} from './src/util/hooks';
import ProfileScreen from './src/screens/ProfileScreen';
import HomeScreen from './src/screens/HomeScreen';
import {Text, View} from 'react-native';
import {Modal, Portal} from 'react-native-paper';
import axios from 'axios';

const Tab = createMaterialBottomTabNavigator();

const App = () => {
  const isLogin: boolean = useAppSelector(
    store => store.authentication.isLogin,
  );
  let [e, setE] = useState<string | undefined>(undefined);
  useEffect(() => {
    axios.interceptors.response.use(
      response => {
        return response;
      },
      error => {
        setE(error.message);
      },
    );
  }, []);
  return (
    <>
      <Portal>
        <Modal visible={e !== undefined} onDismiss={() => setE(undefined)}>
          <View style={{flexGrow: 1}}>
            <Text>{e}</Text>
          </View>
        </Modal>
      </Portal>
      <NavigationContainer>
        <Tab.Navigator>
          <Tab.Screen name="Home" component={HomeScreen} />
          <Tab.Screen name="Notes" component={NoteScreen} />
          <Tab.Screen name="Canvas" component={CanvasScreen} />
          <Tab.Screen name="Profile" component={ProfileScreen} />
        </Tab.Navigator>
      </NavigationContainer>
    </>
  );
};

export default App;
