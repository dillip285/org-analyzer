package com.dillip.org;

import java.util.List;

import com.dillip.org.Models.ReportingLineViolation;
import com.dillip.org.Models.SalaryViolation;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Please provide the CSV file path as an argument");
                return;
            }

            OrgAnalyzer analyzer = new OrgAnalyzer();
            analyzer.loadData(args[0]);

            System.out.println("\nAnalyzing salary violations...");
            List<SalaryViolation> salaryViolations = analyzer.analyzeSalaries();
            salaryViolations.forEach(System.out::println);
            System.out.println("Analyzing salary violations completed.");

            System.out.println("\nAnalyzing reporting line violations...");
            List<ReportingLineViolation> lineViolations = analyzer.analyzeReportingLines();
            lineViolations.forEach(System.out::println);
            System.out.println("Analyzing reporting line violations completed.");
            
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}