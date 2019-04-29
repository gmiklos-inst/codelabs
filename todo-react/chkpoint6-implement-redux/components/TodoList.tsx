import React, { Component } from 'react';

import { default as Table, THead, Tr, Th, TBody } from '@inst/bridge-ui-components.table';
import { TodoItem } from '../model/todoItem';
import { TodoItemRow } from './TodoItemRow';

export type TodoListProps = {
  todoItems: TodoItem[];
  onToggleTodo: (id) => void;
  onDeleteTodo: (id) => void;
};

export class TodoList extends Component<TodoListProps> {
  render() {
    const { onToggleTodo, onDeleteTodo, todoItems } = this.props;
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
            todoItems.map((todoItem) =>
              <TodoItemRow 
                item={todoItem} 
                key={todoItem.id} 
                onToggle={() => onToggleTodo(todoItem.id) } 
                onDelete={() => onDeleteTodo(todoItem.id) } 
              />
            )
          }
        </TBody>
      </Table>
    </div>;
  }
}