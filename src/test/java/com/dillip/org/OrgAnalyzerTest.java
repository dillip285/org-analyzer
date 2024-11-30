package com.dillip.org;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.dillip.org.Models.ReportingLineViolation;
import com.dillip.org.Models.SalaryViolation;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class OrgAnalyzerTest {
    private OrgAnalyzer analyzer;
    private static final String TEST_FILE = "src/test/resources/test_data.csv";
    /*
     * 	Analyzing salary violations...
		Manager Emily Wilson (303) salary $65000.00 is below minimum (should be between $67950.00 and $84937.50)
		Manager Lisa Anderson (405) salary $65250.00 is above maximum (should be between $49800.00 and $62250.00)
		Manager Jennifer White (408) salary $35000.00 is below minimum (should be between $38400.00 and $48000.00)
		Manager William Martin (409) salary $32000.00 is below minimum (should be between $63000.00 and $78750.00)
		Analyzing salary violations completed.
		
		Analyzing reporting line violations...
		Employee Joe Doe (123) has 6 managers in reporting line (maximum allowed is 5 including CEO)
		Employee Martin Chekov (124) has 6 managers in reporting line (maximum allowed is 5 including CEO)
		Analyzing reporting line violations completed.

     */
    

    @BeforeEach
    void setUp() throws Exception {
        analyzer = new OrgAnalyzer();
        analyzer.loadData(TEST_FILE);
    }

    @Test
    void testLoadData() {
        // Test file loading and verify data structure
        assertNotNull(analyzer);
        assertDoesNotThrow(() -> analyzer.loadData(TEST_FILE));
    }
    
    @Test
    void testSalaryViolations() {
        List<SalaryViolation> violations = analyzer.analyzeSalaries();
        
        assertNotNull(violations);
        assertFalse(violations.isEmpty(), "Should find at least one salary violation");
        
        // Emily Wilson's violation (ID: 303)
        boolean foundEmilyViolation = violations.stream()
            .anyMatch(v -> v.getManager().getId() == 303);
        assertTrue(foundEmilyViolation, "Should find salary violation for Emily Wilson");
        
        // Test Emily's specific violation details
        SalaryViolation emilyViolation = violations.stream()
            .filter(v -> v.getManager().getId() == 303)
            .findFirst()
            .orElse(null);
        
        assertNotNull(emilyViolation);
        assertEquals(65000.0, emilyViolation.getCurrentSalary());
        assertTrue(emilyViolation.getCurrentSalary() < emilyViolation.getRequiredMinimum(),
            "Emily's salary should be below minimum");
        
        // Lisa Anderson's violation (ID: 405)
        boolean foundLisaViolation = violations.stream()
            .anyMatch(v -> v.getManager().getId() == 405);
        assertTrue(foundLisaViolation, "Should find salary violation for Lisa Anderson");
        
        // Verify Lisa's violation is for being over maximum
        SalaryViolation lisaViolation = violations.stream()
            .filter(v -> v.getManager().getId() == 405)
            .findFirst()
            .orElse(null);
        assertNotNull(lisaViolation);
        assertTrue(lisaViolation.getCurrentSalary() > lisaViolation.getRequiredMaximum(),
            "Lisa's salary should be above maximum");
        
        // Jennifer White's violation (ID: 408)
        boolean foundJenniferViolation = violations.stream()
            .anyMatch(v -> v.getManager().getId() == 408);
        assertTrue(foundJenniferViolation, "Should find salary violation for Jennifer White");
    }

    @Test
    void testReportingLineViolations() {
        List<ReportingLineViolation> violations = analyzer.analyzeReportingLines();
        
        assertNotNull(violations);
        assertFalse(violations.isEmpty(), "Should find at least one reporting line violation");
        
        // Test Joe's violation (ID: 123)
        boolean foundJoeViolation = violations.stream()
            .anyMatch(v -> v.getEmployee().getId() == 123);
        assertTrue(foundJoeViolation, "Should find reporting line violation for Joe Doe");
        
        // Test Martin's violation (ID: 124)
        boolean foundMartinViolation = violations.stream()
            .anyMatch(v -> v.getEmployee().getId() == 124);
        assertTrue(foundMartinViolation, "Should find reporting line violation for Martin Chekov");
        
        // Verify the reporting line length for Joe
        ReportingLineViolation joeViolation = violations.stream()
            .filter(v -> v.getEmployee().getId() == 123)
            .findFirst()
            .orElse(null);
        
        assertNotNull(joeViolation);
        assertEquals(6, joeViolation.getLineLength(),
            "Joe should have 6 managers in reporting line (including CEO)");
    }
    
    @Test
    void testInvalidFilePath() {
        assertThrows(Exception.class, 
            () -> analyzer.loadData("nonexistent.csv"));
    }

    @Test
    void testEmptyOrganization() throws Exception {
        String emptyFile = "src/test/resources/empty_org.csv";
        OrgAnalyzer emptyAnalyzer = new OrgAnalyzer();
        emptyAnalyzer.loadData(emptyFile);
        
        assertTrue(emptyAnalyzer.analyzeSalaries().isEmpty());
        assertTrue(emptyAnalyzer.analyzeReportingLines().isEmpty());
    }

}