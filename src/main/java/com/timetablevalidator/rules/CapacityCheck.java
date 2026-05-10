package com.timetablevalidator.rules;

import java.util.ArrayList;
import java.util.List;

import com.timetablevalidator.model.Assignment;

public class CapacityCheck implements ConstraintRule {
    @Override
    public String getRuleName() {
        return "Room Capacity Check";
    }

    @Override
    public List<String> validate(List<Assignment> assignments) {
        List<String> conflicts = new ArrayList<>();

        for (Assignment assignment : assignments) {
            if (assignment.getStudentGroup().getSize() > assignment.getRoom().getCapacity()) {
                conflicts.add("Room " + assignment.getRoom().getRoomNumber() +
                        " capacity exceeded for " +
                        assignment.getStudentGroup().getGroupName());
            }
        }
        return conflicts;
    }
}