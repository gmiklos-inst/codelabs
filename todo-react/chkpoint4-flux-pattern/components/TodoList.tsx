import React, { Component } from 'react';

import { default as Table, THead, Tr, Th, TBody } from '@inst/bridge-ui-components.table';
import { TodoItem } from '../model/todoItem';
import { TodoItemRow } from './TodoItemRow';

export type TodoListProps = {
  todoItems: TodoItem[];
};

export class TodoList extends Component<TodoListProps> {
  render() {
    return <div className="todo-list" data-testid="TodoList">
      <Table responsive hover>
        <THead>
          <Tr>
            <Th>
              Check
            </Th>
            <Th>
              Item
            </Th>
            <Th>
              Action
            </Th>
          </Tr>
        </THead>
        <TBody>
          {
            this.props.todoItems.map((todoItem) =>
              <TodoItemRow item={todoItem} key={todoItem.id} />
            )
          }
        </TBody>
      </Table>
    </div>;
  }
}