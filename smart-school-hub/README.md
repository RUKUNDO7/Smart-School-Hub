# Smart School Hub

Spring Boot + JPA + Postgres scaffold for a School Management System with JWT auth and a React UI.

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- Spring Web + Spring Data JPA + Spring Security
- PostgreSQL
- React (Vite)

## Backend Run
Set environment variables or use defaults:
- `DB_URL` (default `jdbc:postgresql://localhost:5432/smartschoolhub`)
- `DB_USER` (default `postgres`)
- `DB_PASSWORD` (default `postgres`)
- `JWT_SECRET` (default `change-this-secret-in-prod`)

```
mvn spring-boot:run
```

### Default Admin
- Username: `admin`
- Password: `admin123`

## Frontend Run
```
cd frontend
npm install
npm run dev
```

## Core Modules & Endpoints
Base path: `/api`

- Auth: `/auth/login`, `/auth/register`
- Students: `/students`
- Teachers: `/teachers`
- Classes: `/classes`
- Subjects: `/subjects`
- Student Attendance: `/attendance`
- Teacher Attendance: `/teacher-attendance`
- Exams: `/exams`
- Grades: `/grades`
- Fees: `/fees`
- Teacher Evaluations: `/teacher-evaluations`
- Timetable: `/timetable`
- Dashboard summary: `/dashboard/summary`
- Reports: `/reports/*`
- Parent portal (student progress): `/parent-portal/students/{studentId}/progress`

## Notes
- `classes` table is mapped by the `SchoolClass` entity.
- Schema is generated automatically by JPA (`spring.jpa.hibernate.ddl-auto=update`).
- JWT is required for all `/api/**` routes except `/api/auth/**`.
