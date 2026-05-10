package com.timetablevalidator.model;

public class Assignment {
    private int id;
    private String courseCode;
    private String courseTitle;
    private Teacher teacher;
    private Room room;
    private StudentGroup studentGroup;
    private TimeSlot timeSlot;

    public Assignment() {
    }

    public Assignment(int id, String courseCode, String courseTitle,
            Teacher teacher, Room room,
            StudentGroup studentGroup, TimeSlot timeSlot) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.teacher = teacher;
        this.room = room;
        this.studentGroup = studentGroup;
        this.timeSlot = timeSlot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}