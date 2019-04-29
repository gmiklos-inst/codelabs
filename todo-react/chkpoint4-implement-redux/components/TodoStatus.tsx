import React, { Component } from 'react';

import { default as Chip } from '@inst/bridge-ui-components.chip';

export type TodoStatusProps = {
  itemLeftCount: number;
};

export class TodoStatus extends Component<TodoStatusProps> {
    render() {
        const label = this.props.itemLeftCount > 1 ? `${this.props.itemLeftCount} items left` : this.props.itemLeftCount === 1 ? `1 item left` : `no items left`;
        return <Chip label={label}  data-testid="TodoStatus"/>;
    }
}