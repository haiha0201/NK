import {useState} from 'react';
import {
  Gesture,
  GestureStateChangeEvent,
  GestureUpdateEvent,
  PanGestureHandlerEventPayload,
} from 'react-native-gesture-handler';
import {SkiaMutableValue, SkMatrix} from '@shopify/react-native-skia';
import {runOnJS} from 'react-native-reanimated';

export interface Point {
  x: number;
  y: number;
}

export interface IPath {
  segments: Point[];
  start: Point;
  color: string;
}

const randomNumber = (): string => {
  const generateRandomColor = Math.floor(Math.random() * 16777215)
    .toString(16)
    .padStart(6, '0');
  return `#${generateRandomColor}`;
};

function useDraw(matrix: SkiaMutableValue<SkMatrix>) {
  const [paths, setPaths] = useState<IPath[]>([]);
  const newPath = (
    g: GestureStateChangeEvent<PanGestureHandlerEventPayload>,
  ) => {
    setPaths(prevState => {
      const randomColor = randomNumber();
      let newVar = matrix.current.get();
      return [
        ...prevState,
        {
          start: {x: g.x - newVar[2], y: g.y - newVar[5]},
          segments: [],
          color: randomColor,
        },
      ];
    });
  };
  const addPath = (g: GestureUpdateEvent<PanGestureHandlerEventPayload>) => {
    setPaths(prevState => {
      const index = paths.length - 1;
      let newVar = matrix.current.get();
      if (!prevState[index]) {
        return prevState;
      }
      prevState[index].segments.push({x: g.x - newVar[2], y: g.y - newVar[5]});
      return [...prevState];
    });
  };

  const pan = Gesture.Pan()
    .onBegin(g => {
      runOnJS(newPath)(g);
    })
    .onChange(g => {
      runOnJS(addPath)(g);
    })
    .minDistance(10);

  return {paths: paths, setPaths: setPaths, panHandler: pan};
}

export default useDraw;
