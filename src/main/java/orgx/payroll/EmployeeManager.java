package orgx.payroll;

import java.util.*;

public class EmployeeManager {
    private final Map<Integer, Employee> employees = new LinkedHashMap<>();

    public String addEmployee(Employee e) {
        if (e == null) return "ERROR: Employee record is missing.";
        if (e.getName() == null || e.getName().trim().isEmpty())
            return "ERROR: Name cannot be empty.";
        if (e.getDepartment() == null || e.getDepartment().trim().isEmpty())
            return "ERROR: Department cannot be empty.";
        if (e.getHourlyRate() < 0)
            return "ERROR: Hourly rate cannot be negative.";
        if (e.getHoursWorked() < 0)
            return "ERROR: Hours worked cannot be negative.";
        if (employees.containsKey(e.getId()))
            return "ERROR: Employee ID is already in use.";
        employees.put(e.getId(), e);
        return "Employee saved successfully.";
    }

    public boolean removeEmployee(int id) { return employees.remove(id) != null; }
    public Employee getEmployee(int id) { return employees.get(id); }
    public List<Employee> listEmployees() { return new ArrayList<>(employees.values()); }
    public int count() { return employees.size(); }
    public void clear() { employees.clear(); }
}