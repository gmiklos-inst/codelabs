
import React, { Component } from 'react';

import { RadioButtonGroup, default as RadioButton  } from '@inst/bridge-ui-components.radio-button';

export enum TodoFilterState {
  ALL,
  ACTIVE,
  COMPLETED,
}

export type TodoFilterProps = {
  todoFilterState: TodoFilterState;
  onChange: (filterState: TodoFilterState) => void;
};

export class TodoFilter extends Component<TodoFilterProps> {
    render() {
        return <RadioButtonGroup 
          selected={this.props.todoFilterState}
          onChange={filterState => this.props.onChange(filterState)}
        >
          <RadioButton
            label="All"
            value={TodoFilterState.ALL}
          />
          <RadioButton
            label="Active"
            value={TodoFilterState.ACTIVE}
          />
          <RadioButton
            label="Completed"
            value={TodoFilterState.COMPLETED}
          />
        </RadioButtonGroup>;
    }
}