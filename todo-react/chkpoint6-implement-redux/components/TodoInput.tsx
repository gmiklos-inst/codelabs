import React, { Component } from 'react';

import { default as TextInput } from '@inst/bridge-ui-components.text-input';
import { EditIcon } from '@inst/bridge-ui-components.icon';

export type TodoInputProps = {
  onChange: (text: string) => void;
  onSubmit: () => void;
  value: string;
};

export class TodoInput extends Component<TodoInputProps> {
    render() {
      const { onChange, onSubmit, value } = this.props;
      return <TextInput
        data-testid="TodoInput"
        value={value}
        onChange={event => onChange(event.target.value)}
        onKeyUp={(event) => {
          if (event.charCode === 13 || event.keyCode === 13) {
            onSubmit();
          }
        }}
        label="Add new TODO item here"
        icon={<EditIcon />}
      />;
    }
}