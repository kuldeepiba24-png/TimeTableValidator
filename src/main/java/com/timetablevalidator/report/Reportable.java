package com.timetablevalidator.report;

import java.util.List;

public interface Reportable {
    void generateReport(List<String> conflicts);
}