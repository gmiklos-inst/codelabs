let nextTodoId = 0

export const addTodo = (text: string) => ({
  type: 'ADD_TODO',
  id: nextTodoId++,
  text
})

export const setVisibilityFilter = (filter: VisibilityFilters) => ({
  type: 'SET_VISIBILITY_FILTER',
  filter
})

export const toggleTodo = (id: string) => ({
  type: 'TOGGLE_TODO',
  id
})

enum VisibilityFilters {
    ShowAll = 'SHOW_ALL',
    ShowCompleted = 'SHOW_COMPLETED',
    ShowActive = 'SHOW_ACTIVE'
}
