package com.timetablevalidator.rules;

import java.util.List;

import com.timetablevalidator.model.Assignment;

public interface ConstraintRule {
    List<String> validate(List<Assignment> assignments);

    String getRuleName();
}