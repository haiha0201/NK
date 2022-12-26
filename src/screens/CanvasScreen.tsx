import {
  Canvas,
  Path,
  Skia,
  SkiaDomView,
  SkRect,
  useValue,
} from '@shopify/react-native-skia';
import React, {createRef} from 'react';
import {View} from 'react-native';
import useDraw, {IPath, Point} from '../util/useDraw';
import {Gesture, GestureDetector} from 'react-native-gesture-handler';
import {GestureHandler} from '../util/GestureHandler';
import RNFS from 'react-native-fs';
import {FAB} from 'react-native-paper';

const renderPath = (path: IPath) => {
  const render = `M ${path.start.x} ${path.start.y} ${path.segments
    .map(el => `L ${el.x} ${el.y}`)
    .join(' ')}`;
  return render;
};

const getBoundary = (paths: IPath[]): SkRect => {
  const points: Point[] = paths.flatMap(el => [el.start, ...el.segments]);
  const xs = [...points.map(el => el.x)];
  const ys = [...points.map(el => el.y)];
  const xMin = Math.min(...xs);
  const xMax = Math.max(...xs);
  const yMin = Math.min(...ys);
  const yMax = Math.max(...ys);
  console.log(xMax - xMin);
  console.log(`${xMin} ${yMin} ${xMax} ${yMax}`);
  // return {
  //   x: xMin,
  //   y: yMin,
  //   width: xMax - xMin,
  //   height: yMax - yMin,
  // };
  return {x: 0, y: 400, width: 400, height: 800};
  // return {x: -250, y: -250, width: 500, height: 500};
};

const CanvasScreen: React.FC = () => {
  const matrix = useValue(Skia.Matrix());
  const {paths, panHandler} = useDraw(matrix);
  const canvasRef = createRef<SkiaDomView>();

  function saveImage() {
    // const image: SkImage | undefined = canvasRef.current?.makeImageSnapshot(
    //   getBoundary(paths),
    // );
    // if (image) {
    //   const bytes = image.encodeToBase64(ImageFormat.PNG);
    //   const path = RNFS.PicturesDirectoryPath + '/test.png';
    //   RNFS.writeFile(path, bytes, 'base64').catch(console.log);
    // }
    const path = RNFS.DocumentDirectoryPath + '/path.txt';
    let s = JSON.stringify(paths);
    RNFS.writeFile(path, s).catch(console.log);
  }

  return (
    <View style={{flex: 1}}>
      <GestureHandler matrix={matrix}>
        <GestureDetector gesture={Gesture.Race(panHandler)}>
          <View style={{flex: 1}}>
            <Canvas style={{flex: 8}} ref={canvasRef}>
              {paths.map((p, index) => (
                <Path
                  matrix={matrix}
                  key={index}
                  path={renderPath(p)}
                  strokeWidth={5}
                  style={'stroke'}
                  color={p.color}
                />
              ))}
            </Canvas>
          </View>
        </GestureDetector>
      </GestureHandler>
      <FAB
        icon="plus"
        onPress={saveImage}
        style={{
          position: 'absolute',
          margin: 16,
          right: 0,
          bottom: 0,
        }}
      />
    </View>
  );
};
export default CanvasScreen;
