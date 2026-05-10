package com.timetablevalidator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import com.timetablevalidator.services.TeacherService;
import com.timetablevalidator.model.Teacher;
import java.util.List;

public class TeacherInputPanel extends JPanel {
    private JTextField nameField, departmentField;
    private JButton addButton, deleteButton, refreshButton;
    private JTable teacherTable;
    private DefaultTableModel tableModel;

    public TeacherInputPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        inputPanel.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2, true));
        UIStyles.styleTitledBorder(inputPanel, "Add New Teacher");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(UIStyles.LABEL_FONT);
        nameLabel.setForeground(UIStyles.DARK_TEXT);
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setFont(UIStyles.LABEL_FONT);
        nameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(nameField, gbc);

        // Department
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(UIStyles.LABEL_FONT);
        deptLabel.setForeground(UIStyles.DARK_TEXT);
        inputPanel.add(deptLabel, gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(15);
        departmentField.setFont(UIStyles.LABEL_FONT);
        departmentField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(departmentField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        addButton = UIStyles.createAddButton("Add Teacher");
        deleteButton = UIStyles.createDeleteButton("Delete Selected");
        refreshButton = UIStyles.createRefreshButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table for displaying teachers
        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Department" }, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teacherTable = new JTable(tableModel);
        UIStyles.styleTableWithTimesNewRoman(teacherTable);

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addTeacher());
        deleteButton.addActionListener(e -> deleteTeacher());
        refreshButton.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void addTeacher() {
        String name = nameField.getText().trim();
        String department = departmentField.getText().trim();

        if (name.isEmpty() || department.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (TeacherService.addTeacher(name, department)) {
            JOptionPane.showMessageDialog(this, "Teacher added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            departmentField.setText("");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding teacher", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a teacher to delete", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (TeacherService.deleteTeacher(id)) {
            JOptionPane.showMessageDialog(this, "Teacher deleted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting teacher", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Teacher> teachers = TeacherService.getAllTeachers();
            for (Teacher teacher : teachers) {
                tableModel.addRow(new Object[] { teacher.getId(), teacher.getName(), teacher.getDepartment() });
            }
        } catch (Exception e) {
            // Database connection error - table will remain empty
        }
    }
}
