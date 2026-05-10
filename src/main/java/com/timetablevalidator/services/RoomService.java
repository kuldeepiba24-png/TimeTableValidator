package com.timetablevalidator.services;

import com.timetablevalidator.database.DBHelper;
import com.timetablevalidator.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomService {

    public static boolean addRoom(String roomNumber, int capacity) {
        String sql = "INSERT INTO rooms (room_number, capacity) VALUES (?, ?)";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            pstmt.setInt(2, capacity);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DBHelper.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getInt("capacity"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
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
