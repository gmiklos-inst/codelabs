import React, { Component } from 'react';

import { TodoItem } from '../model/todoItem';
import { Tr, Td } from '@inst/bridge-ui-components.table';
import { default as Checkbox } from '@inst/bridge-ui-components.checkbox';
import { IconButton } from '@inst/bridge-ui-components.icon-button';
import { ClearIcon } from '@inst/bridge-ui-components.icon';

export type TodoItemRowProps = {
  item: TodoItem;
  onToggle: () => void;
  onDelete: () => void;
};

export class TodoItemRow extends Component<TodoItemRowProps> {
    render() {
      const { item, onToggle, onDelete } = this.props;
        return <Tr>
          <Td>
            <Checkbox 
              checked={item.completed} 
              onChange={() => onToggle()} 
            />
          </Td>
          <Td>
            {item.title}
          </Td>
          <Td>
            <IconButton 
              label="Clear" 
              filled 
              icon={<ClearIcon />} 
              onClick={() => onDelete()}
            />
          </Td>
        </Tr>;
    }
}