import React from 'react';
import {NoteInterface} from '../entity/NoteInterface';
import {Avatar, Button, Card, Paragraph} from 'react-native-paper';

const LeftContent = (props: any) => (
  <Avatar.Icon {...props} icon="comment-outline" />
);

const NoteView: React.FC<{
  data: NoteInterface;
  onClick: () => void;
}> = props => {
  return (
    <Card>
      <Card.Title
        title={props.data?.title}
        subtitle={props.data?.date}
        left={LeftContent}
      />
      <Card.Content>
        <Paragraph>{props.data?.content}</Paragraph>
      </Card.Content>
      <Card.Actions>
        <Button mode={'elevated'} onTouchEnd={props.onClick}>
          Edit
        </Button>
      </Card.Actions>
    </Card>
  );
};

export default NoteView;
