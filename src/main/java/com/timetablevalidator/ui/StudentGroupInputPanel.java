package com.timetablevalidator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import com.timetablevalidator.services.StudentGroupService;
import com.timetablevalidator.model.StudentGroup;
import java.util.List;

public class StudentGroupInputPanel extends JPanel {
    private JTextField groupNameField, sizeField;
    private JButton addButton, deleteButton, refreshButton;
    private JTable groupTable;
    private DefaultTableModel tableModel;

    public StudentGroupInputPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        UIStyles.styleTitledBorder(inputPanel, "Add New Student Group");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel groupLabel = new JLabel("Group Name:");
        groupLabel.setFont(UIStyles.LABEL_FONT);
        groupLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(groupLabel, gbc);
        gbc.gridx = 1;
        groupNameField = new JTextField(15);
        groupNameField.setFont(UIStyles.LABEL_FONT);
        groupNameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(groupNameField, gbc);

        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setFont(UIStyles.LABEL_FONT);
        sizeLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(sizeLabel, gbc);
        gbc.gridx = 1;
        sizeField = new JTextField(15);
        sizeField.setFont(UIStyles.LABEL_FONT);
        sizeField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(sizeField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = UIStyles.createAddButton("Add Group");
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

        tableModel = new DefaultTableModel(new String[]{"ID", "Group Name", "Size"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        groupTable = new JTable(tableModel);
        styleTable(groupTable);
        
        JScrollPane scrollPane = new JScrollPane(groupTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addGroup());
        deleteButton.addActionListener(e -> deleteGroup());
        refreshButton.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void styleTable(JTable table) {
        UIStyles.styleTableWithTimesNewRoman(table);
    }

    private void addGroup() {
        String groupName = groupNameField.getText().trim();
        String sizeStr = sizeField.getText().trim();

        if (groupName.isEmpty() || sizeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int size = Integer.parseInt(sizeStr);
            if (StudentGroupService.addStudentGroup(groupName, size)) {
                JOptionPane.showMessageDialog(this, "Student group added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                groupNameField.setText("");
                sizeField.setText("");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding group", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Size must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteGroup() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a group to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (StudentGroupService.deleteStudentGroup(id)) {
            JOptionPane.showMessageDialog(this, "Student group deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting group", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<StudentGroup> groups = StudentGroupService.getAllStudentGroups();
            for (StudentGroup group : groups) {
                tableModel.addRow(new Object[]{group.getId(), group.getGroupName(), group.getSize()});
            }
        } catch (Exception e) {
            // Database connection error - table will remain empty
        }
    }
}
