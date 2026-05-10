package com.timetablevalidator.report;

import java.util.List;

public class ConflictReport implements Reportable {
    @Override
    public void generateReport(List<String> conflicts) {
        System.out.println("===== TIMETABLE CONFLICT REPORT =====");
        if (conflicts.isEmpty()) {
            System.out.println("No conflicts detected.");
        } else {
            for (String conflict : conflicts) {
                System.out.println(conflict);
            }
        }
    }
}