import React, {useState} from 'react';
import {Controller, useForm} from 'react-hook-form';
import {Button, Text, TextInput} from 'react-native-paper';
import {View} from 'react-native';
import {Authentication} from '../entity/Authentication';
import {handleLogin} from '../util/handleAuthentication';

const minLength = 8;

function SignIn(props: {onPress: () => void}) {
  return (
    <>
      <Text style={{fontSize: 40}}>Sign in</Text>
      <View
        style={{
          flexDirection: 'row',
          alignItems: 'center',
        }}>
        <Text>No account yet?</Text>
        <Button onPress={props.onPress}>Sign up</Button>
      </View>
    </>
  );
}

function SignupComp(props: {onPress: () => void}) {
  return (
    <>
      <Text style={{fontSize: 40}}>Sign up</Text>
      <View
        style={{
          flexDirection: 'row',
          alignItems: 'center',
        }}>
        <Text>Already have an account? </Text>
        <Button onPress={props.onPress}>Sign in</Button>
      </View>
    </>
  );
}

const HomeScreen: React.FC<{}> = () => {
  const [isLoginScreen, setIsLoginScreen] = useState<boolean>(true);
  const {
    handleSubmit,
    control,
    formState: {errors},
  } = useForm<Authentication>({
    mode: 'onChange',
    defaultValues: {
      username: undefined,
      password: undefined,
    },
  });
  return (
    <View style={{paddingTop: 50, paddingHorizontal: 10}}>
      <View style={{marginBottom: 50}}>
        {isLoginScreen && <SignIn onPress={() => setIsLoginScreen(false)} />}
        {!isLoginScreen && (
          <SignupComp onPress={() => setIsLoginScreen(true)} />
        )}
      </View>
      <View style={{marginBottom: 50}}>
        <Controller
          control={control}
          rules={{
            required: {value: true, message: 'Username is required'},
          }}
          render={({field: {onChange, onBlur, value}}) => (
            <TextInput
              onBlur={onBlur}
              onChangeText={onChange}
              value={value}
              placeholder={'Username'}
            />
          )}
          name="username"
        />
        {errors.username && <Text>{errors.username.message}</Text>}
      </View>

      <View style={{marginBottom: 50}}>
        <Controller
          control={control}
          rules={{
            required: {value: true, message: 'Password is required'},
            minLength: {
              value: minLength,
              message: `Min length is ${minLength}`,
            },
          }}
          render={({field: {onChange, onBlur, value}}) => (
            <TextInput
              secureTextEntry={true}
              onBlur={onBlur}
              onChangeText={onChange}
              value={value}
              placeholder={'Password'}
            />
          )}
          name="password"
        />
        {errors.password && <Text>{errors.password.message}</Text>}
      </View>

      <Button
        style={{alignSelf: 'center', marginBottom: 20, marginTop: 'auto'}}
        onPress={handleSubmit(handleLogin, console.log)}
        mode={'outlined'}>
        Submit
      </Button>
    </View>
  );
};
export default HomeScreen;
