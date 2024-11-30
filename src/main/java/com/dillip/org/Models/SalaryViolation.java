package com.dillip.org.Models;
public class SalaryViolation {
    private Employee manager;
    private double requiredMinimum;
    private double requiredMaximum;
    private double currentSalary;

    public SalaryViolation(Employee manager, double requiredMinimum, double requiredMaximum, double currentSalary) {
        this.manager = manager;
        this.requiredMinimum = requiredMinimum;
        this.requiredMaximum = requiredMaximum;
        this.currentSalary = currentSalary;
    }

    // Getters
    public Employee getManager() { return manager; }
    public double getRequiredMinimum() { return requiredMinimum; }
    public double getRequiredMaximum() { return requiredMaximum; }
    public double getCurrentSalary() { return currentSalary; }

    @Override
    public String toString() {
        String violationType = currentSalary < requiredMinimum ? "below minimum" : "above maximum";
        return String.format("Manager %s %s (%d) salary $%.2f is %s (should be between $%.2f and $%.2f)",
            manager.getFirstName(),
            manager.getLastName(),
            manager.getId(),
            currentSalary,
            violationType,
            requiredMinimum,
            requiredMaximum);
    }
}