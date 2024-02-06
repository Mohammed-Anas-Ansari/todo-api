# todo-api


## DB Table

ToDoInfo (1) ------------------------ (n) ToDoTask

### ToDoInfo
- id: number _(PK)_
- title: string
- isDeleted: boolean
- createdAt: string
- updatedAt: string
- toDoTasks: ToDoTask[]

### ToDoTask
- id: number _(PK)_
- task: string
- isCompleted: boolean
- toDoInfoId: number _(FK)_