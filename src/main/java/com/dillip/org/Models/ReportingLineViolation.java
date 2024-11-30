package com.dillip.org.Models;
public class ReportingLineViolation {
    private Employee employee;
    private int lineLength;

    public ReportingLineViolation(Employee employee, int lineLength) {
        this.employee = employee;
        this.lineLength = lineLength;
    }

    public Employee getEmployee() { return employee; }
    public int getLineLength() { return lineLength; }

    @Override
    public String toString() {
        return String.format("Employee %s %s (%d) has %d managers in reporting line (maximum allowed is 5 including CEO)",
            employee.getFirstName(),
            employee.getLastName(),
            employee.getId(),
            lineLength);
    }
}