# TaskBoard

**TaskBoard** is a modern Android to-do application built using **Clean Architecture**, **MVVM**, and **Jetpack Compose**.  
It follows reactive data patterns using **Kotlin Flows**, **Room**, **Hilt**, and **StateFlow**, designed to showcase scalable, modular, and testable Android app development.

---

## Features

- Add, edit, and delete tasks  
- Mark tasks as done/undone  
- Real-time reactive list updates  
- Offline-first local caching  
- Sync tasks with a fake network API  
- Smooth navigation between Task List and Add/Edit Task screens  
- Material 3 UI

---

## Architecture Overview

TaskBoard follows **Clean + MVVM Architecture** organized into distinct layers for maintainability and scalability:

---

## 🔄 Reactive Data Flow

The app uses **Kotlin Flow** for end-to-end reactivity:

```text
Room (Flow<List<TaskEntity>>)
   ↓ map
Repository (Flow<List<Task>>)
   ↓ collect
ViewModel (StateFlow<ResultState>)
   ↓ observeAsState()
Composable UI (recomposes automatically)
```

### Example
When a task is added or updated:
1. `TaskDao` emits the latest list through its `Flow`.
2. `TaskRepositoryImpl` transforms `TaskEntity` → `Task` domain models.
3. `TaskListViewModel` updates `_uiState` → triggers recomposition in Compose.
4. The UI reflects new data instantly — **no manual reload needed**.

---

## Module Highlights

### `core-common`
Contains shared utilities and types:
- **ResultState** for representing UI/data states. 
- Common extensions, constants, and helpers used across modules.

### `core-network`
Handles remote data operations:
- **FakeNetworkApi** simulates network calls. 
- Defines network DTOs and mappings to domain models.

### `core-data`
Handles all data persistence and transformation:
- **Task DAO** for local caching.
- **TaskRepositoryImpl** mediates between local and network sources.
- Uses **Hilt** for providing repository and database dependencies.

### `feature-taskboard`
Encapsulates task-related functionality:
- **TaskListScreen** → Displays all tasks.
- **AddEditTaskScreen** → Add or edit existing task.
- Each screen has its own **ViewModel** for clear separation of concerns.

---

## Tech Stack

| Category | Technology |
|-----------|-------------|
| UI | Jetpack Compose + Material3 |
| State Management | StateFlow + collectAsStateWithLifecycle |
| Dependency Injection | Hilt |
| Database | Room |
| Concurrency | Kotlin Coroutines |
| Navigation | Navigation Compose |
| Architecture | Clean + MVVM |
| Language | Kotlin |

---

## UI Highlights

- **Reactive `LazyColumn`** displaying `TaskItem`s with fade-in/out animations.  
- Each task shows:
  - Checkbox to toggle completion  
  - Title and optional description  
  - Timestamp for last update  
  - Delete icon  
- **Line-through effect** applied to completed tasks using `TextDecoration.LineThrough`.  
- **Animated Snackbar** for sync success feedback.  
- **Floating Action Button** for adding new tasks.  

---

## Repository Implementation Details

`TaskRepositoryImpl` provides a unified data source:

The repository ensures:
- Data flows **reactively**.
- Local cache is always kept consistent with remote (fake) data.
- Operations are **offloaded to IO dispatcher** for performance.

---

## ViewModel Responsibilities

### TaskListViewModel
- Observes all tasks as `StateFlow<ResultState<List<Task>>>`
- Handles toggling, deletion, and sync operations.
- Exposes `SyncState` to show syncing progress and results.

### AddEditTaskViewModel
- Fetches task by ID (for editing).
- Validates and inserts/updates task in the repository.
- Emits navigation or success events via `MutableStateFlow`.

---

## Design Decisions

- **Each screen has its own ViewModel** — avoids shared mutable state between screens.  
- **Reactive data observation** — eliminates need for manual refreshes.  
- **Immutable `ResultState` sealed class** for clear UI rendering logic.  
- **Dependency inversion** — `TaskRepository` interface in domain, implemented in `core-data`.  
- **Hilt provides dependencies** across feature and core modules seamlessly.  

---