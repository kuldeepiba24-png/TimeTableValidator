package com.timetablevalidator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.timetablevalidator.services.*;
import com.timetablevalidator.model.*;
import java.util.List;

public class AssignmentInputPanel extends JPanel {
    private JTextField courseCodeField, subjectField;
    private JComboBox<Teacher> teacherCombo;
    private JComboBox<StudentGroup> groupCombo;
    private JComboBox<TimeSlot> timeSlotCombo;
    private JButton addButton, deleteButton, refreshButton;
    private JTable assignmentTable;
    private DefaultTableModel tableModel;

    public AssignmentInputPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        UIStyles.styleTitledBorder(inputPanel, "Add New Assignment (Room Auto-Allocated)");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel codeLabel = new JLabel("Course Code:");
        codeLabel.setFont(UIStyles.LABEL_FONT);
        codeLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(codeLabel, gbc);
        gbc.gridx = 1;
        courseCodeField = new JTextField(15);
        courseCodeField.setFont(UIStyles.LABEL_FONT);
        courseCodeField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(courseCodeField, gbc);

        JLabel subjectLabel = new JLabel("Subject/Title:");
        subjectLabel.setFont(UIStyles.LABEL_FONT);
        subjectLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        subjectField = new JTextField(15);
        subjectField.setFont(UIStyles.LABEL_FONT);
        subjectField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(subjectField, gbc);

        JLabel teacherLabel = new JLabel("Teacher:");
        teacherLabel.setFont(UIStyles.LABEL_FONT);
        teacherLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(teacherLabel, gbc);
        gbc.gridx = 1;
        teacherCombo = new JComboBox<>();
        teacherCombo.setFont(UIStyles.LABEL_FONT);
        inputPanel.add(teacherCombo, gbc);

        JLabel groupLabel = new JLabel("Student Group:");
        groupLabel.setFont(UIStyles.LABEL_FONT);
        groupLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(groupLabel, gbc);
        gbc.gridx = 1;
        groupCombo = new JComboBox<>();
        groupCombo.setFont(UIStyles.LABEL_FONT);
        inputPanel.add(groupCombo, gbc);

        JLabel timeLabel = new JLabel("Time Slot:");
        timeLabel.setFont(UIStyles.LABEL_FONT);
        timeLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(timeLabel, gbc);
        gbc.gridx = 1;
        timeSlotCombo = new JComboBox<>();
        timeSlotCombo.setFont(UIStyles.LABEL_FONT);
        inputPanel.add(timeSlotCombo, gbc);

        // Info panel showing auto-allocation
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(230, 244, 255));
        infoPanel.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 1));
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel("Room will be automatically assigned based on availability and capacity");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(UIStyles.PRIMARY_COLOR);
        infoPanel.add(infoLabel);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(infoPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = UIStyles.createAddButton("Add Assignment");
        deleteButton = UIStyles.createDeleteButton("Delete Selected");
        refreshButton = UIStyles.createRefreshButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Course Code", "Subject", "Teacher", "Room", "Group", "Time Slot"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        assignmentTable = new JTable(tableModel);
        styleTable(assignmentTable);
        
        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addAssignment());
        deleteButton.addActionListener(e -> deleteAssignment());
        refreshButton.addActionListener(e -> refreshData());

        refreshData();
    }

    private void styleTable(JTable table) {
        UIStyles.styleTableWithTimesNewRoman(table);
    }

    private void addAssignment() {
        String courseCode = courseCodeField.getText().trim();
        String subject = subjectField.getText().trim();
        Teacher teacher = (Teacher) teacherCombo.getSelectedItem();
        StudentGroup group = (StudentGroup) groupCombo.getSelectedItem();
        TimeSlot timeSlot = (TimeSlot) timeSlotCombo.getSelectedItem();

        if (courseCode.isEmpty() || subject.isEmpty() || teacher == null || group == null || timeSlot == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Try to find available room automatically
        Room availableRoom = RoomAllocationService.findAvailableRoom(group, timeSlot);
        if (availableRoom == null) {
            JOptionPane.showMessageDialog(this, 
                "No available room with sufficient capacity (" + group.getSize() + " students) for the selected time slot!", 
                "Allocation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if teacher is available
        if (!RoomAllocationService.isTeacherAvailable(teacher.getId(), timeSlot)) {
            JOptionPane.showMessageDialog(this, 
                "Teacher is not available at the selected time slot (already has an assignment)!", 
                "Conflict Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add assignment with automatically assigned room
        if (AssignmentService.addAssignment(courseCode, subject, teacher.getId(), availableRoom.getId(), group.getId(), timeSlot.getId())) {
            JOptionPane.showMessageDialog(this, 
                "Assignment added successfully!\nCourse: " + courseCode + "\nRoom: " + availableRoom.getRoomNumber() + " (Auto-Allocated)", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            courseCodeField.setText("");
            subjectField.setText("");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding assignment", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an assignment to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (AssignmentService.deleteAssignment(id)) {
            JOptionPane.showMessageDialog(this, "Assignment deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting assignment", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        // Refresh combo boxes
        refreshCombos();
        // Refresh table
        refreshTable();
    }

    private void refreshCombos() {
        teacherCombo.removeAllItems();
        groupCombo.removeAllItems();
        timeSlotCombo.removeAllItems();

        try {
            for (Teacher t : TeacherService.getAllTeachers()) {
                teacherCombo.addItem(t);
            }
            for (StudentGroup sg : StudentGroupService.getAllStudentGroups()) {
                groupCombo.addItem(sg);
            }
            for (TimeSlot ts : TimeSlotService.getAllTimeSlots()) {
                timeSlotCombo.addItem(ts);
            }
        } catch (Exception e) {
            // Database connection error - combos will remain empty
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Assignment> assignments = AssignmentService.getAllAssignments();
            for (Assignment a : assignments) {
                tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getCourseCode(),
                    a.getCourseTitle(),
                    a.getTeacher().getName(),
                    a.getRoom().getRoomNumber(),
                    a.getStudentGroup().getGroupName(),
                    a.getTimeSlot().getDay() + " " + a.getTimeSlot().getStartTime()
                });
            }
        } catch (Exception e) {
            // Database connection error - table will remain empty
        }
    }
}
