package orgx.payroll;

import java.util.List;

public class ReportGenerator {
    private final PayrollProcessor payroll = new PayrollProcessor();

    public String employeeReport(Employee e) {
        double gross = payroll.calculateGrossPay(e);
        double tax = payroll.calculateTax(gross);
        double net = gross - tax;
        return String.format("ID=%d | %s | Gross=%.2f | Tax=%.2f | Net=%.2f",
                e.getId(), e.getName(), gross, tax, net);
    }

    public String payrollReport(List<Employee> employees) {
        if (employees.isEmpty()) return "NO EMPLOYEES TO REPORT.";
        double total=0, top=-1; Employee topEmployee=null;
        StringBuilder sb=new StringBuilder("PAYROLL REPORT\n");
        for(Employee e:employees){
            double net=payroll.calculateNetPay(e);
            total+=net;
            if(net>top){top=net; topEmployee=e;}
            sb.append(employeeReport(e)).append("\n");
        }
        double avg=total/employees.size();
        sb.append(String.format("Total payroll (net): %.2f\nAverage net pay: %.2f\nTop earner: %s (%.2f)",
                total,avg,topEmployee.getName(),top));
        return sb.toString();
    }
}