package orgx.payroll;

public class Employee {
    private final int id;
    private final String name;
    private final String department;
    private final double hourlyRate;
    private double hoursWorked;

    public Employee(int id, String name, String department, double hourlyRate, double hoursWorked) {
        this.id = id; this.name = name; this.department = department;
        this.hourlyRate = hourlyRate; this.hoursWorked = hoursWorked;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getDepartment(){ return department; }
    public double getHourlyRate(){ return hourlyRate; }
    public double getHoursWorked(){ return hoursWorked; }
    public void setHoursWorked(double hoursWorked){ this.hoursWorked = hoursWorked; }

    @Override public String toString() {
        return String.format("ID=%d | Name=%s | Department=%s | Rate=%.2f | Hours=%.2f",
                id,name,department,hourlyRate,hoursWorked);
    }
}