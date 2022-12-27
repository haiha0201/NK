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
import {createMaterialBottomTabNavigator} from '@react-navigation/material-bottom-tabs';
import {useAppSelector} from './src/util/hooks';
import axios from 'axios';
import CanvasScreen from './src/screens/CanvasScreen';
import HomeScreen from './src/screens/HomeScreen';
import {NavigationContainer} from '@react-navigation/native';
import NoteScreen from './src/screens/NoteScreen';
import ProfileScreen from './src/screens/ProfileScreen';

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
      {/*<Portal>*/}
      {/*  <Modal*/}
      {/*    visible={e !== undefined}*/}
      {/*    onDismiss={() => setE(undefined)}*/}
      {/*    dismissable={true}*/}
      {/*    contentContainerStyle={{backgroundColor: 'white', padding: 20}}>*/}
      {/*    <Text>{e}</Text>*/}
      {/*  </Modal>*/}
      {/*</Portal>*/}
      {!isLogin && <HomeScreen />}
      {isLogin && (
        <NavigationContainer>
          <Tab.Navigator>
            <Tab.Screen name="Notes" component={NoteScreen} />
            <Tab.Screen name="Canvas" component={CanvasScreen} />
            <Tab.Screen name="Profile" component={ProfileScreen} />
          </Tab.Navigator>
        </NavigationContainer>
      )}
    </>
  );
};

export default App;
