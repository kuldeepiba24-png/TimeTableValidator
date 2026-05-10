package com.timetablevalidator.model;

public class StudentGroup {
    private int id;
    private String groupName;
    private int size;

    public StudentGroup() {
    }

    public StudentGroup(int id, String groupName, int size) {
        this.id = id;
        this.groupName = groupName;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return groupName + " (" + size + " students)";
    }
}