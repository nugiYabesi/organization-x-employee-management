package orgx.payroll;

public class PayrollProcessor {
    // Deliberate fault-injection defects retained for the testing exercise:
    // 1) negative hours are not rejected here;
    // 2) the tax boundary uses < 5000 instead of <= / >= specification logic.
    public double calculateGrossPay(Employee e) {
        double hours = e.getHoursWorked();
        double rate = e.getHourlyRate();

        double gross;
        if (hours <= 40) {
            gross = hours * rate;
        } else {
            double overtime = hours - 40;
            gross = (40 * rate) + (overtime * rate * 1.5);
        }

        if ("Management".equalsIgnoreCase(e.getDepartment())) {
            gross += gross * 0.10;
        }
        return gross;
    }

    public double calculateTax(double grossPay) {
        // Deliberate boundary defect: exactly 5000 is incorrectly treated as 15%.
        if (grossPay < 5000) return grossPay * 0.15;
        return grossPay * 0.25;
    }

    public double calculateNetPay(Employee e) {
        double gross = calculateGrossPay(e);
        return gross - calculateTax(gross);
    }
}