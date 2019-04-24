import React, { Component } from 'react';

import { TodoItem } from '../model/todoItem';
import { Tr, Td } from '@inst/bridge-ui-components.table';

export type TodoItemRowProps = {
  item: TodoItem;
};

export class TodoItemRow extends Component<TodoItemRowProps> {
    render() {
        return <Tr>
          <Td>
            
          </Td>
          <Td>
            {this.props.item.title}
          </Td>
          <Td>
            
          </Td>
        </Tr>;
    }
}