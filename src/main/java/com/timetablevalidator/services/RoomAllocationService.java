package com.timetablevalidator.services;

import com.timetablevalidator.database.DBHelper;
import com.timetablevalidator.model.Room;
import com.timetablevalidator.model.StudentGroup;
import com.timetablevalidator.model.TimeSlot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for automatically allocating rooms based on availability and capacity
 */
public class RoomAllocationService {

    /**
     * Finds an available room for the given student group and time slot
     * Considers room capacity and time slot conflicts
     * 
     * @param studentGroup - Student group needing a room
     * @param timeSlot - Time slot for the class
     * @return Available Room or null if no suitable room found
     */
    public static Room findAvailableRoom(StudentGroup studentGroup, TimeSlot timeSlot) {
        List<Room> availableRooms = getAvailableRooms(studentGroup.getSize(), timeSlot);
        
        if (availableRooms.isEmpty()) {
            return null;
        }
        
        // Return the first available room (smallest suitable room)
        return availableRooms.get(0);
    }

    /**
     * Gets all rooms available for the given time slot and capacity requirement
     * 
     * @param requiredCapacity - Minimum capacity needed
     * @param timeSlot - Time slot to check
     * @return List of available rooms sorted by capacity
     */
    public static List<Room> getAvailableRooms(int requiredCapacity, TimeSlot timeSlot) {
        List<Room> availableRooms = new ArrayList<>();
        
        String sql = "SELECT DISTINCT r.id, r.room_number, r.capacity " +
                    "FROM rooms r " +
                    "WHERE r.capacity >= ? " +
                    "AND r.id NOT IN (" +
                    "  SELECT room_id FROM assignments a " +
                    "  JOIN time_slots ts ON a.time_slot_id = ts.id " +
                    "  WHERE ts.day = ? " +
                    "  AND (" +
                    "    (ts.start_time <= ? AND ts.end_time > ?) OR " +
                    "    (ts.start_time < ? AND ts.end_time >= ?) OR " +
                    "    (ts.start_time >= ? AND ts.end_time <= ?)" +
                    "  )" +
                    ") " +
                    "ORDER BY r.capacity ASC";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requiredCapacity);
            pstmt.setString(2, timeSlot.getDay());
            pstmt.setString(3, timeSlot.getStartTime());
            pstmt.setString(4, timeSlot.getStartTime());
            pstmt.setString(5, timeSlot.getEndTime());
            pstmt.setString(6, timeSlot.getEndTime());
            pstmt.setString(7, timeSlot.getStartTime());
            pstmt.setString(8, timeSlot.getEndTime());
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getInt("capacity")
                );
                availableRooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available rooms: " + e.getMessage());
            e.printStackTrace();
        }
        
        return availableRooms;
    }

    /**
     * Checks if a teacher has conflict at the given time slot
     * 
     * @param teacherId - Teacher ID
     * @param timeSlot - Time slot to check
     * @return true if teacher is already assigned at this time, false otherwise
     */
    public static boolean isTeacherAvailable(int teacherId, TimeSlot timeSlot) {
        String sql = "SELECT COUNT(*) as count FROM assignments a " +
                    "JOIN time_slots ts ON a.time_slot_id = ts.id " +
                    "WHERE a.teacher_id = ? " +
                    "AND ts.day = ? " +
                    "AND (" +
                    "  (ts.start_time <= ? AND ts.end_time > ?) OR " +
                    "  (ts.start_time < ? AND ts.end_time >= ?) OR " +
                    "  (ts.start_time >= ? AND ts.end_time <= ?)" +
                    ")";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            pstmt.setString(2, timeSlot.getDay());
            pstmt.setString(3, timeSlot.getStartTime());
            pstmt.setString(4, timeSlot.getStartTime());
            pstmt.setString(5, timeSlot.getEndTime());
            pstmt.setString(6, timeSlot.getEndTime());
            pstmt.setString(7, timeSlot.getStartTime());
            pstmt.setString(8, timeSlot.getEndTime());
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking teacher availability: " + e.getMessage());
            e.printStackTrace();
        }
        
        return true; // Assume available if error occurs
    }

    /**
     * Gets available rooms count for given capacity requirement
     * 
     * @param requiredCapacity - Minimum capacity needed
     * @param timeSlot - Time slot to check
     * @return Number of available rooms
     */
    public static int countAvailableRooms(int requiredCapacity, TimeSlot timeSlot) {
        return getAvailableRooms(requiredCapacity, timeSlot).size();
    }
}
