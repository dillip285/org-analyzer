package com.dillip.org;
import com.dillip.org.Models.Employee;
import com.dillip.org.Models.ReportingLineViolation;
import com.dillip.org.Models.SalaryViolation;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OrgAnalyzer {
    private Map<Integer, Employee> employees = new HashMap<>();
    private static final double MIN_SALARY_MULTIPLIER = 1.20; // 20% more
    private static final double MAX_SALARY_MULTIPLIER = 1.50; // 50% more
    private static final int MAX_REPORTING_LINE = 5; // Excluding CEO we can have 4 Managers (CEO + 4 Managers)

    public void loadData(String filename) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            // Skip header
            reader.readNext();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                Employee emp = new Employee(
                    Integer.parseInt(line[0].trim()),
                    line[1].trim(),
                    line[2].trim(),
                    Double.parseDouble(line[3].trim()),
                    line[4].trim().isEmpty() ? null : Integer.parseInt(line[4].trim())
                );
                employees.put(emp.getId(), emp);
            }
            
            // Build relationships
            buildRelationships();
        }
    }

    private void buildRelationships() {
        for (Employee emp : employees.values()) {
            if (emp.getManagerId() != null) {
                Employee manager = employees.get(emp.getManagerId());
                if (manager != null) {
                    manager.addSubordinate(emp);
                }
            }
        }
    }

    public List<SalaryViolation> analyzeSalaries() {
        List<SalaryViolation> violations = new ArrayList<>();
        
        for (Employee manager : employees.values()) {
            if (!manager.getSubordinates().isEmpty()) {
                double avgSubordinateSalary = manager.getSubordinates().stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0.0);
                
                double minRequired = avgSubordinateSalary * MIN_SALARY_MULTIPLIER;
                double maxAllowed = avgSubordinateSalary * MAX_SALARY_MULTIPLIER;
                
                if (manager.getSalary() < minRequired || manager.getSalary() > maxAllowed) {
                    violations.add(new SalaryViolation(manager, minRequired, maxAllowed, manager.getSalary()));
                }
            }
        }
        
        return violations;
    }

    public List<ReportingLineViolation> analyzeReportingLines() {
        List<ReportingLineViolation> violations = new ArrayList<>();
        
        for (Employee emp : employees.values()) {
            int lineLength = calculateReportingLineLength(emp);
            if (lineLength > MAX_REPORTING_LINE) {
                violations.add(new ReportingLineViolation(emp, lineLength));
            }
        }
        
        return violations;
    }

    private int calculateReportingLineLength(Employee emp) {
        int length = 0;
        Integer currentManagerId = emp.getManagerId();
        
        while (currentManagerId != null) {
            length++;
            Employee manager = employees.get(currentManagerId);
            if (manager == null) break;
            currentManagerId = manager.getManagerId();
        }
        
        return length;
    }
}