
# MaccsEvents

> **DAM2 Mobile Application Development Project**
> 
> A modern Android application for event management, built with Jetpack Compose, Room, and Firebase.

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple) ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-blue) ![Firebase](https://img.shields.io/badge/Backend-Firebase-orange) ![Room](https://img.shields.io/badge/Persistence-Room%20Database-green) ![Status](https://img.shields.io/badge/Status-Phase%205%20Completed-success)

## Overview
**MaccsEvents** is a native Android application designed to manage and discover local events. 
Developed as part of the **Multiplatform Application Development** curriculum, this project focuses on implementing a robust architecture, handling persistent local data, and synchronizing with cloud services.

The project follows a **Local-First** strategy, ensuring a seamless user experience regardless of network connectivity.

---

## Architecture / Stack

The application is built using **Clean Architecture principles** (adapted) and the **MVVM (Model-View-ViewModel)** pattern to ensure separation of concerns and testability.

###  Technology Stack
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Material 3 Design System)
* **Asynchronous Processing:** Coroutines & Flow
* **Dependency Injection:** Manual DI via `AppContainer` 
* **Image Loading:** Coil 
* **Visual Assets:** Stock or uploaded by user

###  Data Layer
**Local Persistence:** Android **Room Database** (Entities & DAOs).
**Remote Backend:** **Firebase** (Firestore & Authentication).
**Repository Pattern:** Acts as the Single Source of Truth, mediating between Room and the ViewModel.

###  Project Structure
The codebase is organized into several layers:
```text
com.maccsevents
├── app/             # Application entry point
├── core/            # Core utilities (data, design, utils)
├── data/            # Repositories, DAOs, Models, Entities
├── di/              # Dependency Injection container
├── models/          # Domain models (Data Classes)
└── ui/              # Screens, Components, ViewModels, Theme

```

---

##  Development Roadmap

The project was executed in an agile environment using **Kanban**, divided into 6 distinct phases.

###  Phase 0: Planning & Setup

* Definition of the roadmap and visual style (Figma).

* Kanban board setup in Notion.

* Git environment configuration (SSH keys, Global User setup).



###  Phase 1: Project Scaffolding

* Repository initialization with Git Flow strategy (`main`, `dev`, `feature/*`).


* Gradle dependency management (Navigation Compose, Room, Firebase).


* Clean Architecture folder structure setup.



###  Phase 2: Data Modeling & UI Prototyping

* Creation of Domain Models (e.g., `Event` data class).


* UI implementation using `LazyColumn` for efficient event lists.


* **Mocking:** Use of mock data to test UI components independently of the backend.



###  Phase 3: ViewModel & State Management

* Migration of business logic to `EventViewModel`.


* Implementation of **UI States** (Loading, Success, Error) using `StateFlow`.


* Interactive logic (Favorites, Attend buttons).



###  Phase 4: Persistence (Room)

* Implementation of SQL Entities (`EventEntity`) and DAOs (`EventDao`).


* Configuration of `AppDatabase`.


* Integration of `viewModelScope` for asynchronous database operations.



###  Phase 5: Integration & Refinement

* **Navigation:** Custom `AppBottomBar` and dynamic routing based on Auth status.


* **Auth Integration:** Replaced static IDs with `FirebaseAuth` UIDs.


* **User Profile:** Local-first strategy for user data management.


---


## Credits

* [@tamasi17](https://github.com/tamasi17)
* [@cebriii](https://github.com/cebriii)
* [@anapope](https://github.com/anapope)
* [@sofiipz](https://github.com/sofiipz)
* [@Ceeciimg](https://github.com/Ceeciimg)

---

 **Course:** DAM2 Development of Mobile Applications 

