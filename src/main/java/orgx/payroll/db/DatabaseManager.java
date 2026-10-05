package orgx.payroll.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Handles PostgreSQL connections and creates the employee table when needed. */
public class DatabaseManager {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseManager() {
        this.url = env("ORGX_DB_URL", "jdbc:postgresql://localhost:5432/organization_x");
        this.username = env("ORGX_DB_USER", "postgres");
        this.password = env("ORGX_DB_PASSWORD", "");
    }

    private String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public Connection getConnection() throws SQLException {
        if (password.isBlank()) {
            throw new SQLException("ORGX_DB_PASSWORD is not set. Set it before starting the application.");
        }
        return DriverManager.getConnection(url, username, password);
    }

    public void initializeDatabase() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS employees (
                    id INTEGER PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    department VARCHAR(100) NOT NULL,
                    hourly_rate NUMERIC(12, 2) NOT NULL CHECK (hourly_rate >= 0),
                    hours_worked NUMERIC(12, 2) NOT NULL CHECK (hours_worked >= 0),
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public String getUrl() {
        return url;
    }
}
