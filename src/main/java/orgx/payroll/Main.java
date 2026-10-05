package orgx.payroll;

import orgx.payroll.db.DatabaseManager;
import orgx.payroll.db.EmployeeRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Organization X interactive employee and payroll application.
 * Normal employee operations use PostgreSQL; testing cases use an isolated in-memory manager.
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final PayrollProcessor payroll = new PayrollProcessor();
    private static final ReportGenerator reports = new ReportGenerator();

    public static void main(String[] args) {
        DatabaseManager database = new DatabaseManager();
        EmployeeRepository repository = new EmployeeRepository(database);

        try {
            database.initializeDatabase();
            System.out.println("PostgreSQL connected successfully: " + database.getUrl());
        } catch (SQLException e) {
            System.out.println("DATABASE CONNECTION ERROR");
            System.out.println(e.getMessage());
            System.out.println("Set ORGX_DB_URL, ORGX_DB_USER and ORGX_DB_PASSWORD, then restart the program.");
            return;
        }

        mainMenu(repository);
    }

    private static void mainMenu(EmployeeRepository repository) {
        while (true) {
            System.out.println("\n============================================================");
            System.out.println("        ORGANIZATION X - EMPLOYEE MANAGEMENT SYSTEM");
            System.out.println("============================================================");
            System.out.println("1. Register Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Find Employee");
            System.out.println("4. Remove Employee");
            System.out.println("5. Calculate Employee Payroll");
            System.out.println("6. Generate Payroll Report");
            System.out.println("7. Black Box Testing");
            System.out.println("8. White Box Testing");
            System.out.println("9. Employee Count");
            System.out.println("0. Exit");
            System.out.println("------------------------------------------------------------");

            String choice = readLine("Enter your choice: ");
            try {
                switch (choice) {
                    case "1" -> registerEmployee(repository);
                    case "2" -> listEmployees(repository);
                    case "3" -> findEmployee(repository);
                    case "4" -> removeEmployee(repository);
                    case "5" -> calculatePayroll(repository);
                    case "6" -> generatePayrollReport(repository);
                    case "7" -> blackBoxMenu();
                    case "8" -> whiteBoxMenu();
                    case "9" -> System.out.println("Total employees: " + repository.count());
                    case "0" -> {
                        System.out.println("Thank you for using Organization X. Goodbye!");
                        return;
                    }
                    default -> System.out.println("ERROR: Please choose a valid option from 0 to 9.");
                }
            } catch (SQLException e) {
                System.out.println("DATABASE ERROR: " + e.getMessage());
            }
        }
    }

    private static void registerEmployee(EmployeeRepository repository) {
        System.out.println("\n---------------- REGISTER EMPLOYEE ----------------");
        int id = readInt("Employee ID: ");
        String name = readLine("Full name: ");
        String department = readLine("Department: ");
        double rate = readNonNegativeDouble("Hourly rate: ");
        double hours = readNonNegativeDouble("Hours worked: ");

        Employee employee = new Employee(id, name, department, rate, hours);
        System.out.println(repository.addEmployee(employee));
    }

    private static void listEmployees(EmployeeRepository repository) throws SQLException {
        System.out.println("\n---------------- ALL EMPLOYEES ----------------");
        List<Employee> employees = repository.listEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees have been registered yet.");
            return;
        }
        for (Employee employee : employees) System.out.println(employee);
        System.out.println("Total: " + employees.size());
    }

    private static void findEmployee(EmployeeRepository repository) throws SQLException {
        int id = readInt("Enter employee ID to find: ");
        Employee employee = repository.getEmployee(id);
        if (employee == null) {
            System.out.println("Employee ID " + id + " does not exist.");
            return;
        }
        System.out.println("Employee found:");
        System.out.println(employee);
    }

    private static void removeEmployee(EmployeeRepository repository) throws SQLException {
        int id = readInt("Enter employee ID to remove: ");
        if (repository.removeEmployee(id)) {
            System.out.println("Employee " + id + " removed successfully.");
        } else {
            System.out.println("Employee ID " + id + " does not exist.");
        }
    }

    private static void calculatePayroll(EmployeeRepository repository) throws SQLException {
        int id = readInt("Enter employee ID: ");
        Employee employee = repository.getEmployee(id);
        if (employee == null) {
            System.out.println("Employee ID " + id + " does not exist.");
            return;
        }
        System.out.println("\n---------------- PAYROLL ----------------");
        System.out.println(reports.employeeReport(employee));
    }

    private static void generatePayrollReport(EmployeeRepository repository) throws SQLException {
        System.out.println("\n---------------- PAYROLL REPORT ----------------");
        System.out.println(reports.payrollReport(repository.listEmployees()));
    }

    private static void blackBoxMenu() {
        while (true) {
            System.out.println("\n--------------- BLACK BOX TESTING ---------------");
            System.out.println("1. TC-BB-01 - Save valid employee");
            System.out.println("2. TC-BB-02 - Reject blank employee name");
            System.out.println("3. TC-BB-03 - Reject negative hours worked");
            System.out.println("4. TC-BB-04 - Reject duplicate employee ID");
            System.out.println("5. TC-BB-05 - Remove existing employee");
            System.out.println("6. TC-BB-06 - Report missing employee ID");
            System.out.println("7. TC-BB-07 - Calculate overtime + management bonus + tax");
            System.out.println("8. TC-BB-08 - Payroll report, retrieval, listing and count");
            System.out.println("9. Interactive test employee input");
            System.out.println("0. Back to main menu");
            String c = readLine("Choose a black-box test: ");

            switch (c) {
                case "1" -> bb01();
                case "2" -> bb02();
                case "3" -> bb03();
                case "4" -> bb04();
                case "5" -> bb05();
                case "6" -> bb06();
                case "7" -> bb07();
                case "8" -> bb08();
                case "9" -> interactiveValidationTest();
                case "0" -> { return; }
                default -> System.out.println("ERROR: Invalid black-box option.");
            }
            pause();
        }
    }

    private static void whiteBoxMenu() {
        while (true) {
            System.out.println("\n--------------- WHITE BOX TESTING ---------------");
            System.out.println("1. TC-WB-01 - addEmployee() valid path");
            System.out.println("2. TC-WB-02 - addEmployee() validation branch");
            System.out.println("3. TC-WB-03 - addEmployee() duplicate-ID branch");
            System.out.println("4. TC-WB-04 - removeEmployee() true branch");
            System.out.println("5. TC-WB-05 - removeEmployee() false branch");
            System.out.println("6. TC-WB-06 - calculateGrossPay() regular-hours branch");
            System.out.println("7. TC-WB-07 - calculateGrossPay() overtime branch");
            System.out.println("8. TC-WB-08 - calculateTax() boundary branches");
            System.out.println("9. Interactive validation path");
            System.out.println("0. Back to main menu");
            String c = readLine("Choose a white-box test: ");

            switch (c) {
                case "1" -> wb01();
                case "2" -> wb02();
                case "3" -> wb03();
                case "4" -> wb04();
                case "5" -> wb05();
                case "6" -> wb06();
                case "7" -> wb07();
                case "8" -> wb08();
                case "9" -> interactiveValidationTest();
                case "0" -> { return; }
                default -> System.out.println("ERROR: Invalid white-box option.");
            }
            pause();
        }
    }

    private static Employee e(int id, String name, String dept, double rate, double hours) {
        return new Employee(id, name, dept, rate, hours);
    }

    private static void result(String expected, String actual, boolean pass) {
        System.out.println("EXPECTED: " + expected);
        System.out.println("ACTUAL:   " + actual);
        System.out.println("RESULT:   " + (pass ? "PASS" : "FAIL"));
    }

    private static void bb01() {
        EmployeeManager m = new EmployeeManager();
        String a = m.addEmployee(e(101, "Alice", "Finance", 20, 40));
        result("Employee is saved and count becomes 1.", a + " | count=" + m.count(),
                a.startsWith("Employee saved") && m.count() == 1);
    }

    private static void bb02() {
        EmployeeManager m = new EmployeeManager();
        String a = m.addEmployee(e(102, "   ", "Finance", 20, 40));
        result("Registration is rejected because name is empty.", a, a.startsWith("ERROR"));
    }

    private static void bb03() {
        EmployeeManager m = new EmployeeManager();
        String a = m.addEmployee(e(103, "Bob", "IT", 20, -5));
        result("Registration is rejected because hours worked are negative.", a, a.startsWith("ERROR"));
    }

    private static void bb04() {
        EmployeeManager m = new EmployeeManager();
        m.addEmployee(e(104, "Carol", "HR", 20, 40));
        String a = m.addEmployee(e(104, "David", "IT", 30, 40));
        result("Second record is rejected because ID 104 already exists.", a, a.contains("already"));
    }

    private static void bb05() {
        EmployeeManager m = new EmployeeManager();
        m.addEmployee(e(105, "Eve", "IT", 20, 40));
        boolean removed = m.removeEmployee(105);
        result("Existing employee is removed and retrieval returns null.",
                "removed=" + removed + " | retrieved=" + m.getEmployee(105),
                removed && m.getEmployee(105) == null);
    }

    private static void bb06() {
        EmployeeManager m = new EmployeeManager();
        boolean removed = m.removeEmployee(999);
        result("System reports that ID 999 does not exist.",
                removed ? "Employee removed." : "Employee ID 999 does not exist.", !removed);
    }

    private static void bb07() {
        EmployeeManager m = new EmployeeManager();
        Employee x = e(107, "Frank", "Management", 80, 51.25);
        m.addEmployee(x);
        double gross = payroll.calculateGrossPay(x);
        double tax = payroll.calculateTax(gross);
        double net = payroll.calculateNetPay(x);
        result("Gross=5005.00, Tax=1251.25, Net=3753.75",
                String.format("Gross=%.2f, Tax=%.2f, Net=%.2f", gross, tax, net),
                Math.abs(gross - 5005) < .001 && Math.abs(tax - 1251.25) < .001 && Math.abs(net - 3753.75) < .001);
    }

    private static void bb08() {
        EmployeeManager m = new EmployeeManager();
        m.addEmployee(e(108, "Grace", "IT", 20, 40));
        m.addEmployee(e(109, "Henry", "Management", 40, 45));
        String retrieved = m.getEmployee(108).getName();
        String report = reports.payrollReport(m.listEmployees());
        boolean pass = m.count() == 2 && retrieved.equals("Grace") && report.contains("Total payroll") && report.contains("Average net pay") && report.contains("Top earner");
        result("Two employees are listed/retrievable; count=2; report contains total, average and top earner.",
                "count=" + m.count() + " | retrieved=" + retrieved + " | report generated=" + report.contains("PAYROLL REPORT"), pass);
        System.out.println(report);
    }

    private static void wb01() {
        EmployeeManager m = new EmployeeManager();
        String a = m.addEmployee(e(201, "Ian", "IT", 20, 40));
        result("All validation conditions are false and put() executes.", a + " | size=" + m.count(), a.startsWith("Employee saved") && m.count() == 1);
    }

    private static void wb02() {
        EmployeeManager m = new EmployeeManager();
        String a = m.addEmployee(e(202, " ", "IT", 20, 40));
        result("Name-empty validation branch returns an error before insertion.", a + " | size=" + m.count(), a.contains("Name cannot") && m.count() == 0);
    }

    private static void wb03() {
        EmployeeManager m = new EmployeeManager();
        m.addEmployee(e(203, "Jack", "IT", 20, 40));
        String a = m.addEmployee(e(203, "Kate", "IT", 20, 40));
        result("containsKey(id) is true and duplicate branch returns an error.", a + " | size=" + m.count(), a.contains("already") && m.count() == 1);
    }

    private static void wb04() {
        EmployeeManager m = new EmployeeManager();
        m.addEmployee(e(204, "Liam", "HR", 20, 40));
        boolean r = m.removeEmployee(204);
        result("removeEmployee() true branch is taken.", String.valueOf(r), r);
    }

    private static void wb05() {
        EmployeeManager m = new EmployeeManager();
        boolean r = m.removeEmployee(205);
        result("removeEmployee() false branch is taken for a missing ID.", String.valueOf(r), !r);
    }

    private static void wb06() {
        Employee x = e(206, "Mia", "IT", 10, 40);
        double g = payroll.calculateGrossPay(x);
        result("hours <= 40 branch executes; gross=400.00", String.format("Gross=%.2f", g), Math.abs(g - 400) < .001);
    }

    private static void wb07() {
        Employee x = e(207, "Noah", "IT", 10, 41);
        double g = payroll.calculateGrossPay(x);
        result("hours > 40 branch executes; gross=415.00", String.format("Gross=%.2f", g), Math.abs(g - 415) < .001);
    }

    private static void wb08() {
        double below = payroll.calculateTax(4999);
        double boundary = payroll.calculateTax(5000);
        double above = payroll.calculateTax(5001);
        boolean pass = Math.abs(below - 749.85) < .001 && Math.abs(boundary - 1250) < .001 && Math.abs(above - 1250.25) < .001;
        result("4999 uses 15%; 5000 and above use 25%.", String.format("4999=%.2f | 5000=%.2f | 5001=%.2f", below, boundary, above), pass);
    }

    private static void interactiveValidationTest() {
        System.out.println("\n----------- INTERACTIVE TEST INPUT -----------");
        System.out.println("Enter the employee values you want to test.");
        int id = readInt("Employee ID: ");
        String name = readLineAllowBlank("Name (press ENTER to test blank-name validation): ");
        String department = readLineAllowBlank("Department: ");
        double rate = readDouble("Hourly rate: ");
        double hours = readDouble("Hours worked: ");

        EmployeeManager manager = new EmployeeManager();
        String actual = manager.addEmployee(e(id, name, department, rate, hours));
        System.out.println("\nSystem response: " + actual);
        System.out.println("Stored employee count: " + manager.count());
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static String readLineAllowBlank(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                double value = Double.parseDouble(readLine(prompt));
                if (Double.isNaN(value) || Double.isInfinite(value)) throw new NumberFormatException();
                return value;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Enter a valid number, for example 20 or 20.50.");
            }
        }
    }

    private static double readNonNegativeDouble(String prompt) {
        while (true) {
            double value = readDouble(prompt);
            if (value >= 0) return value;
            System.out.println("ERROR: Value cannot be negative.");
        }
    }

    private static void pause() {
        System.out.println("\nPress ENTER to continue...");
        sc.nextLine();
    }
}
