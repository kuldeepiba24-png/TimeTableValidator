package com.timetablevalidator.services;

import com.timetablevalidator.database.DBHelper;
import com.timetablevalidator.model.TimeSlot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotService {

    public static boolean addTimeSlot(String day, String startTime, String endTime) {
        String sql = "INSERT INTO time_slots (day, start_time, end_time) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, day);
            pstmt.setString(2, startTime);
            pstmt.setString(3, endTime);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        String sql = "SELECT * FROM time_slots";
        try (Connection conn = DBHelper.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TimeSlot timeSlot = new TimeSlot(
                        rs.getInt("id"),
                        rs.getString("day"),
                        rs.getString("start_time"),
                        rs.getString("end_time"));
                timeSlots.add(timeSlot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timeSlots;
    }

    public static boolean deleteTimeSlot(int id) {
        String sql = "DELETE FROM time_slots WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
