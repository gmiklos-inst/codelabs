import React, { Component } from 'react';

import { default as TextInput } from '@inst/bridge-ui-components.text-input';
import { EditIcon } from '@inst/bridge-ui-components.icon';

export class TodoInput extends Component {
    render() {
        return <TextInput
        value="TODO item text"
        label="Add new TODO item here"
        icon={<EditIcon />}
      />;
    }
}