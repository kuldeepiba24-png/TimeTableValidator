package com.timetablevalidator.services;

import com.timetablevalidator.database.DBHelper;
import com.timetablevalidator.model.Teacher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherService {

    public static boolean addTeacher(String name, String department) {
        String sql = "INSERT INTO teachers (name, department) VALUES (?, ?)";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, department);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers";
        try (Connection conn = DBHelper.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"));
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    public static boolean deleteTeacher(int id) {
        String sql = "DELETE FROM teachers WHERE id = ?";
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
