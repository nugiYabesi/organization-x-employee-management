# Organization X — Interactive Employee Management & Testing Console

A Java 17 console application for Software Testing Techniques, upgraded with a real interactive employee-management workflow and PostgreSQL persistence.

## Features

### Employee Management
- Register an employee using values entered at runtime.
- Store employees in PostgreSQL.
- View all employees.
- Find an employee by ID.
- Remove an employee by ID.
- Calculate individual payroll.
- Generate a complete payroll report.
- Count registered employees.

### Software Testing
- Black Box Testing with 8 documented test cases.
- White Box Testing with 8 documented test cases.
- Interactive validation testing where the tester enters the employee values.
- The original testing logic uses an in-memory manager so test cases remain isolated and repeatable.

## Technologies

- Java 17
- Maven
- PostgreSQL
- JDBC
- SQL

## Project structure

```text
src/main/java/orgx/payroll/
├── Main.java
├── Employee.java
├── EmployeeManager.java
├── PayrollProcessor.java
├── ReportGenerator.java
└── db/
    ├── DatabaseManager.java
    └── EmployeeRepository.java

database/
└── schema.sql
```

## PostgreSQL setup

1. Open pgAdmin or psql.
2. Run `database/schema.sql` to create the database:

```sql
CREATE DATABASE organization_x;
```

3. Make sure your PostgreSQL server is running.
4. Set these Windows environment variables. **Do not put your real password in GitHub.**

```text
ORGX_DB_URL=jdbc:postgresql://localhost:5432/organization_x
ORGX_DB_USER=postgres
ORGX_DB_PASSWORD=YOUR_POSTGRES_PASSWORD
```

The application automatically creates the `employees` table the first time it connects.

### Windows PowerShell example

For the current terminal session:

```powershell
$env:ORGX_DB_URL="jdbc:postgresql://localhost:5432/organization_x"
$env:ORGX_DB_USER="postgres"
$env:ORGX_DB_PASSWORD="YOUR_POSTGRES_PASSWORD"
```

Then verify:

```powershell
$env:ORGX_DB_URL
$env:ORGX_DB_USER
```

## Run the application

From the project directory:

```bash
mvn clean compile
mvn exec:java
```

Or on Windows, use:

```text
run.bat
```

## Main menu

```text
1. Register Employee
2. View All Employees
3. Find Employee
4. Remove Employee
5. Calculate Employee Payroll
6. Generate Payroll Report
7. Black Box Testing
8. White Box Testing
9. Employee Count
0. Exit
```

The normal application is interactive: **you enter the employee ID, name, department, hourly rate, and hours worked.** The values are stored in PostgreSQL and remain available after the program is restarted.

## Testing defects

The payroll processor intentionally retains the two defects from the original testing exercise:

1. Negative hours are not rejected inside `calculateGrossPay()` itself.
2. The tax boundary uses `< 5000`, allowing the testing exercise to demonstrate boundary-value analysis at exactly 5000.

These defects are retained intentionally for the Software Testing assignment. They should not be silently removed because the test cases are designed to demonstrate defect detection.

## GitHub safety

Never commit your PostgreSQL password. The project `.gitignore` excludes common local-secret files, and the README uses placeholders rather than real credentials.
