import type {SkiaMutableValue, SkMatrix} from '@shopify/react-native-skia';
import {Skia, useSharedValueEffect} from '@shopify/react-native-skia';
import React, {PropsWithChildren} from 'react';
import {Gesture, GestureDetector} from 'react-native-gesture-handler';
import {useSharedValue} from 'react-native-reanimated';
import {identity4, Matrix4, multiply4, toMatrix3} from 'react-native-redash';

import {concat, vec3} from './MatrixHelpers';

interface GestureHandlerProps {
  matrix: SkiaMutableValue<SkMatrix>;
  debug?: boolean;
}

export const GestureHandler: React.FC<
  GestureHandlerProps & PropsWithChildren
> = props => {
  const origin = useSharedValue(vec3(0, 0, 0));
  const matrix = useSharedValue(identity4);
  const offset = useSharedValue(identity4);

  useSharedValueEffect(() => {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    props.matrix.current = Skia.Matrix(toMatrix3(matrix.value) as any);
  }, matrix);

  const pan = Gesture.Pan()
    .onChange(e => {
      matrix.value = multiply4(
        Matrix4.translate(e.changeX, e.changeY, 0),
        matrix.value,
      );
    })
    .minPointers(2);

  const scale = Gesture.Pinch()
    .onBegin(e => {
      origin.value = [e.focalX, e.focalY, 0];
      offset.value = matrix.value;
    })
    .onChange(e => {
      matrix.value = concat(offset.value, origin.value, [{scale: e.scale}]);
    });

  return (
    <GestureDetector gesture={Gesture.Race(pan, scale)}>
      {props.children}
    </GestureDetector>
  );
};
