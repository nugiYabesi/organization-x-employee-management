package orgx.payroll.db;

import orgx.payroll.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** PostgreSQL CRUD operations for employees. */
public class EmployeeRepository {
    private final DatabaseManager database;

    public EmployeeRepository(DatabaseManager database) {
        this.database = database;
    }

    public String addEmployee(Employee employee) {
        String validation = validate(employee);
        if (validation != null) return validation;

        String sql = "INSERT INTO employees (id, name, department, hourly_rate, hours_worked) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, employee.getId());
            statement.setString(2, employee.getName().trim());
            statement.setString(3, employee.getDepartment().trim());
            statement.setBigDecimal(4, java.math.BigDecimal.valueOf(employee.getHourlyRate()));
            statement.setBigDecimal(5, java.math.BigDecimal.valueOf(employee.getHoursWorked()));
            statement.executeUpdate();
            return "Employee saved successfully to PostgreSQL.";
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                return "ERROR: Employee ID is already in use.";
            }
            return "DATABASE ERROR: " + e.getMessage();
        }
    }

    public boolean removeEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    public Employee getEmployee(int id) throws SQLException {
        String sql = "SELECT id, name, department, hourly_rate, hours_worked FROM employees WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) return null;
                return fromResult(result);
            }
        }
    }

    public List<Employee> listEmployees() throws SQLException {
        String sql = "SELECT id, name, department, hourly_rate, hours_worked FROM employees ORDER BY id";
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) employees.add(fromResult(result));
        }
        return employees;
    }

    public int count() throws SQLException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM employees");
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getInt(1);
        }
    }

    private Employee fromResult(ResultSet result) throws SQLException {
        return new Employee(
                result.getInt("id"),
                result.getString("name"),
                result.getString("department"),
                result.getDouble("hourly_rate"),
                result.getDouble("hours_worked")
        );
    }

    private String validate(Employee e) {
        if (e == null) return "ERROR: Employee record is missing.";
        if (e.getName() == null || e.getName().trim().isEmpty()) return "ERROR: Name cannot be empty.";
        if (e.getDepartment() == null || e.getDepartment().trim().isEmpty()) return "ERROR: Department cannot be empty.";
        if (e.getHourlyRate() < 0) return "ERROR: Hourly rate cannot be negative.";
        if (e.getHoursWorked() < 0) return "ERROR: Hours worked cannot be negative.";
        return null;
    }
}
