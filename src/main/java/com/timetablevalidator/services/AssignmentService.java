package com.timetablevalidator.services;

import com.timetablevalidator.database.DBHelper;
import com.timetablevalidator.model.Assignment;
import com.timetablevalidator.model.Room;
import com.timetablevalidator.model.StudentGroup;
import com.timetablevalidator.model.TimeSlot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentService {

    /**
     * Add assignment with manual room selection
     */
    public static boolean addAssignment(
        String courseCode, String courseTitle,
            int teacherId, int roomId,
            int studentGroupId, int timeSlotId) {
                
        String sql = "INSERT INTO assignments (course_code, course_title, teacher_id, room_id, student_group_id, time_slot_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            pstmt.setString(2, courseTitle);
            pstmt.setInt(3, teacherId);
            pstmt.setInt(4, roomId);
            pstmt.setInt(5, studentGroupId);
            pstmt.setInt(6, timeSlotId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add assignment with automatic room allocation based on availability
     * Room is automatically assigned based on student group capacity and time slot
     * conflicts
     */
    public static boolean addAssignmentWithAutoRoom(String courseCode, String courseTitle,
            int teacherId, int studentGroupId,
            int timeSlotId) {
        try {
            // Fetch student group and time slot details
            StudentGroup group = null;
            TimeSlot timeSlot = null;

            // Get student group
            List<StudentGroup> groups = StudentGroupService.getAllStudentGroups();
            for (StudentGroup sg : groups) {
                if (sg.getId() == studentGroupId) {
                    group = sg;
                    break;
                }
            }

            // Get time slot
            List<TimeSlot> timeSlots = TimeSlotService.getAllTimeSlots();
            for (TimeSlot ts : timeSlots) {
                if (ts.getId() == timeSlotId) {
                    timeSlot = ts;
                    break;
                }
            }

            if (group == null || timeSlot == null) {
                System.err.println("Error: Could not find student group or time slot");
                return false;
            }

            // Check if teacher is available
            if (!RoomAllocationService.isTeacherAvailable(teacherId, timeSlot)) {
                System.err.println("Error: Teacher is not available at this time slot");
                return false;
            }

            // Find available room
            Room availableRoom = RoomAllocationService.findAvailableRoom(group, timeSlot);
            if (availableRoom == null) {
                System.err.println("Error: No available room with sufficient capacity for this time slot");
                return false;
            }

            // Add assignment with the automatically assigned room
            return addAssignment(courseCode, courseTitle, teacherId, availableRoom.getId(),
                    studentGroupId, timeSlotId);

        } catch (Exception e) {
            System.err.println("Error in automatic room allocation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Assignment> getAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT a.*, t.id as tid, t.name, t.department, r.id as rid, r.room_number, r.capacity, " +
                "sg.id as sgid, sg.group_name, sg.size, ts.id as tsid, ts.day, ts.start_time, ts.end_time " +
                "FROM assignments a " +
                "JOIN teachers t ON a.teacher_id = t.id " +
                "JOIN rooms r ON a.room_id = r.id " +
                "JOIN student_groups sg ON a.student_group_id = sg.id " +
                "JOIN time_slots ts ON a.time_slot_id = ts.id";
        try (Connection conn = DBHelper.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Assignment assignment = new Assignment(
                        rs.getInt("a.id"),
                        rs.getString("course_code"),
                        rs.getString("course_title"),
                        new com.timetablevalidator.model.Teacher(rs.getInt("tid"), rs.getString("name"),
                                rs.getString("department")),
                        new com.timetablevalidator.model.Room(rs.getInt("rid"), rs.getString("room_number"),
                                rs.getInt("capacity")),
                        new com.timetablevalidator.model.StudentGroup(rs.getInt("sgid"), rs.getString("group_name"),
                                rs.getInt("size")),
                        new com.timetablevalidator.model.TimeSlot(rs.getInt("tsid"), rs.getString("day"),
                                rs.getString("start_time"), rs.getString("end_time")));
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public static boolean deleteAssignment(int id) {
        String sql = "DELETE FROM assignments WHERE id = ?";
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
