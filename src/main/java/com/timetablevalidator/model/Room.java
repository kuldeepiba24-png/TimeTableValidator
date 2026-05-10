package com.timetablevalidator.model;

public class Room {
    private int id;
    private String roomNumber;
    private int capacity;

    public Room() {
    }

    public Room(int id, String roomNumber, int capacity) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return roomNumber + " (Capacity: " + capacity + ")";
    }
}