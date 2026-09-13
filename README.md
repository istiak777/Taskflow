# TaskFlow

A simple full-stack task manager built as a one-day sprint project.

## Architecture

```
Flutter App (mobile_app/)  --HTTP/JSON-->  Spring Boot API (backend/)  -->  H2 Database
```

- **Backend**: Java + Spring Boot + Spring Data JPA, H2 in-memory database
- **Frontend**: Flutter, calling the REST API via the `http` package
- **Testing**: JUnit5/Mockito (backend), flutter_test/mocktail (frontend)

## Project structure

```
taskflow/
├── backend/          Spring Boot REST API
│   ├── src/main/...  Task entity, repository, service, controller
│   └── src/test/...  Unit + integration tests
└── mobile_app/        Flutter app
    ├── lib/           Task model, API service, UI
    └── test/          Unit + widget tests
```

## What's implemented

- Full CRUD REST API: `GET/POST/PUT/DELETE /api/tasks`
- Flutter UI: list, add, complete, delete tasks
- Automated tests on both backend and frontend

## Running it locally

### Backend
```bash
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`. H2 console available at `/h2-console`.

### Frontend
```bash
cd mobile_app
flutter run -d web-server
```
Open the printed `localhost` URL in a browser. Requires the backend to be running.

### Running tests
```bash
cd backend && mvn test
cd mobile_app && flutter test
```

## Next steps

- Swap H2 for PostgreSQL
- Add authentication (Spring Security + JWT)
- Add CI (GitHub Actions)
- Add state management (Provider/Riverpod/Bloc)
- Deploy backend (Render/Railway)
