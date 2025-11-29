# Issuetracker

Backend for a crowd-sourced mobile issue reporting application (Java + Spring Boot).

## Summary
- **Purpose:** report nearby issues (with location and optional image), track their lifecycle and comments.
- **Backend:** Java + Spring Boot (JPA / Hibernate)
- **Database:** PostgreSQL with PostGIS for location geometry
- **Mobile client:** Flutter (mobile app not included in this repository)

## Key features
- Create and view geolocated issues (PostGIS `Point` column)
- Issue lifecycle tracked via an enum (`IssueStatus`)
- JPA entities, Spring Data JPA repositories and REST endpoints

## Repository layout (top-level)
- `src/main/java/...` — backend Java sources (entities, controllers, services)
- `src/main/resources/application.properties` — runtime configuration
- `pom.xml` — Maven project file
- `compose.yaml` — local development compose (if present)

## Prerequisites
- Java JDK 17 (recommended LTS) — confirm `java -version` on your machine.
  - The project `pom.xml` exposes a `java.version` property; adjust or confirm it if you need to compile with a different JDK.
- Maven 3.8+ (or latest). Verify with `mvn -version`.
- PostgreSQL (13+) with PostGIS extension (geometry column used in `Issue` entity).
- pgAdmin (optional, for GUI DB management).

## Database setup (PostgreSQL + PostGIS)
1. Install PostgreSQL and PostGIS. On Windows you can use the Postgres installer that includes Stack Builder to add PostGIS, or use the distro of your choice.
2. Create the database and a user (replace password values):

```powershell
# run in PowerShell or psql shell (adjust paths/usernames accordingly)
psql -U postgres -c "CREATE DATABASE issue_tracker;"
psql -U postgres -c "CREATE USER tracker_user WITH ENCRYPTED PASSWORD 'tracker_pass';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE issue_tracker TO tracker_user;"
```

3. Enable PostGIS in the new database (run from `psql -d issue_tracker` or pgAdmin query tool):

```sql
CREATE EXTENSION IF NOT EXISTS postgis;
```

4. Configure connection in `src/main/resources/application.properties` (defaults included in the repo):

```
spring.datasource.url=jdbc:postgresql://localhost:5432/issue_tracker
spring.datasource.username=tracker_user
spring.datasource.password=tracker_pass
spring.jpa.hibernate.ddl-auto=update
```

## Notes about schema changes and existing columns
- This project uses `spring.jpa.hibernate.ddl-auto=update` by default. That setting lets Hibernate add new columns and tables but it does not remove or rename existing columns.
- To remove stale/unused columns manually (recommended for production only after reviewing data), use pgAdmin or psql:

```sql
ALTER TABLE issue DROP COLUMN IF EXISTS status;
```

- Alternatively, use a destructive reset in development (DESTROYS DATA):

```properties
# in application.properties (temporary)
spring.jpa.hibernate.ddl-auto=create
```

Then restart the app and switch `ddl-auto` back to `update`.


## Build and run (backend)
1. Build with Maven (from project root):

```powershell
mvn clean install
```

2. Run the application (from project root):

```powershell
mvn spring-boot:run
```

3. Confirm the app is running (defaults):
- The server typically starts on `http://localhost:8080` unless overridden in `application.properties`.

## pgAdmin notes
- Use pgAdmin to connect to `localhost:5432` and the `issue_tracker` database.
- Use the Query Tool to run the DDL statements above (PostGIS extension, ALTER TABLE to remove old columns).

## PostGIS geometry column
- The `Issue` entity declares the location column as `geometry(Point,4326)`; make sure PostGIS is installed and the extension is enabled in the database before the app attempts to persist geometry values.

## Troubleshooting
- If connection gets refused on pgAdmin, then do windows + R and check if Postgres service is running. If not, click on Run.
