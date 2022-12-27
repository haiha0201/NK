import {
  Canvas,
  ImageFormat,
  Path,
  Skia,
  SkiaDomView,
  SkRect,
  useValue,
} from '@shopify/react-native-skia';
import React, {createRef} from 'react';
import {Text, View} from 'react-native';
import useDraw, {IPath, Point} from '../util/useDraw';
import {Gesture, GestureDetector} from 'react-native-gesture-handler';
import RNFS from 'react-native-fs';
import {Button} from 'react-native-paper';

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
  const {paths, setPaths, panHandler} = useDraw(matrix);
  const canvasRef = createRef<SkiaDomView>();

  function clear() {
    setPaths([]);
  }

  function undo() {
    setPaths(prevState => {
      const copy = [...prevState];
      copy.pop();
      return copy;
    });
  }

  function saveImage() {
    // const image: SkImage | undefined = canvasRef.current?.makeImageSnapshot(
    //   getBoundary(paths),
    // );
    // if (image) {
    //   const bytes = image.encodeToBase64(ImageFormat.PNG);
    //   const path = RNFS.PicturesDirectoryPath + '/test.png';
    //   RNFS.writeFile(path, bytes, 'base64').catch(console.log);
    // }
    const myAlbumPath = RNFS.PicturesDirectoryPath;
    if (!canvasRef.current) {
      return;
    }

    RNFS.exists(myAlbumPath)
      .then(value => {
        if (!value) {
          return RNFS.mkdir(myAlbumPath);
        }
      })
      .then(() => {
        const path = myAlbumPath + '/path.png';
        console.log(path);
        let imageSnapshot = canvasRef
          .current!.makeImageSnapshot()
          .encodeToBase64(ImageFormat.PNG);
        return RNFS.writeFile(path, imageSnapshot, 'base64');
      })
      .then(() => console.log('success'))
      .catch(console.log);
  }

  return (
    <>
      <View
        style={{flexDirection: 'row', alignSelf: 'center', marginVertical: 10}}>
        <Button mode={'outlined'} style={{marginRight: 10}} onPress={undo}>
          <Text>Undo</Text>
        </Button>
        <Button mode={'outlined'} style={{marginRight: 10}} onPress={clear}>
          <Text>Clear</Text>
        </Button>
        <Button mode={'outlined'} onPress={saveImage}>
          <Text>Save</Text>
        </Button>
      </View>
      <View style={{flex: 1}}>
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
      </View>
    </>
  );
};
export default CanvasScreen;
