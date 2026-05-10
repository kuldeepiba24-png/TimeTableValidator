package com.timetablevalidator.rules;

import java.util.ArrayList;
import java.util.List;

import com.timetablevalidator.model.Assignment;

public class TeacherConflict implements ConstraintRule {
    @Override
    public String getRuleName() {
        return "Teacher Conflict";
    }

    @Override
    public List<String> validate(List<Assignment> assignments) {
        List<String> conflicts = new ArrayList<>();

        for (int i = 0; i < assignments.size(); i++) {
            for (int j = i + 1; j < assignments.size(); j++) {
                Assignment a = assignments.get(i);
                Assignment b = assignments.get(j);

                if (a.getTeacher().getId() == b.getTeacher().getId() &&
                        a.getTimeSlot().getId() == b.getTimeSlot().getId()) {
                    conflicts.add("Teacher " + a.getTeacher().getName() +
                            " is assigned to multiple classes at " +
                            a.getTimeSlot());
                }
            }
        }
        return conflicts;
    }
}