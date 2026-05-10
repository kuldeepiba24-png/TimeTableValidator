package com.timetablevalidator.rules;

import java.util.ArrayList;
import java.util.List;

import com.timetablevalidator.model.Assignment;

public class RoomConflict implements ConstraintRule {
    @Override
    public String getRuleName() {
        return "Room Conflict";
    }

    @Override
    public List<String> validate(List<Assignment> assignments) {
        List<String> conflicts = new ArrayList<>();

        for (int i = 0; i < assignments.size(); i++) {
            for (int j = i + 1; j < assignments.size(); j++) {
                Assignment a = assignments.get(i);
                Assignment b = assignments.get(j);

                if (a.getRoom().getId() == b.getRoom().getId() &&
                        a.getTimeSlot().getId() == b.getTimeSlot().getId()) {
                    conflicts.add("Room " + a.getRoom().getRoomNumber() +
                            " is double-booked at " + a.getTimeSlot());
                }
            }
        }
        return conflicts;
    }
}