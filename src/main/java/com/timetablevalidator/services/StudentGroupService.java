package com.timetablevalidator.services;

import com.timetablevalidator.database.DBHelper;
import com.timetablevalidator.model.StudentGroup;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentGroupService {

    public static boolean addStudentGroup(String groupName, int size) {
        String sql = "INSERT INTO student_groups (group_name, size) VALUES (?, ?)";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groupName);
            pstmt.setInt(2, size);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<StudentGroup> getAllStudentGroups() {
        List<StudentGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM student_groups";
        try (Connection conn = DBHelper.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StudentGroup group = new StudentGroup(
                        rs.getInt("id"),
                        rs.getString("group_name"),
                        rs.getInt("size"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public static boolean deleteStudentGroup(int id) {
        String sql = "DELETE FROM student_groups WHERE id = ?";
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
