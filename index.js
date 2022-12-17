/**
 * @format
 */

import {AppRegistry} from 'react-native';
import {Provider as PaperProvider} from 'react-native-paper';
import App from './App';
import {name as appName} from './app.json';
import {GestureHandlerRootView} from 'react-native-gesture-handler';
import {QueryClient, QueryClientProvider} from 'react-query';

import store from './src/store/store';
import {Provider} from 'react-redux';

const client = new QueryClient();
export default function Main() {
  return (
    <Provider store={store}>
      <PaperProvider>
        <QueryClientProvider client={client}>
          <GestureHandlerRootView style={{flex: 1}}>
            <App />
          </GestureHandlerRootView>
        </QueryClientProvider>
      </PaperProvider>
    </Provider>
  );
}
AppRegistry.registerComponent(appName, () => Main);
