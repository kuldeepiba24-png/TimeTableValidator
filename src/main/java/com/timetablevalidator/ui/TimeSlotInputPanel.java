package com.timetablevalidator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import com.timetablevalidator.services.TimeSlotService;
import com.timetablevalidator.model.TimeSlot;
import java.util.List;

public class TimeSlotInputPanel extends JPanel {
    private JComboBox<String> dayCombo;
    private JTextField startTimeField, endTimeField;
    private JButton addButton, deleteButton, refreshButton;
    private JTable timeSlotTable;
    private DefaultTableModel tableModel;

    public TimeSlotInputPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        UIStyles.styleTitledBorder(inputPanel, "Add New Time Slot");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dayLabel = new JLabel("Day:");
        dayLabel.setFont(UIStyles.LABEL_FONT);
        dayLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(dayLabel, gbc);
        gbc.gridx = 1;
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        dayCombo = new JComboBox<>(days);
        dayCombo.setFont(UIStyles.LABEL_FONT);
        inputPanel.add(dayCombo, gbc);

        JLabel startLabel = new JLabel("Start Time (HH:MM:SS):");
        startLabel.setFont(UIStyles.LABEL_FONT);
        startLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(startLabel, gbc);
        gbc.gridx = 1;
        startTimeField = new JTextField(15);
        startTimeField.setFont(UIStyles.LABEL_FONT);
        startTimeField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(startTimeField, gbc);

        JLabel endLabel = new JLabel("End Time (HH:MM:SS):");
        endLabel.setFont(UIStyles.LABEL_FONT);
        endLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(endLabel, gbc);
        gbc.gridx = 1;
        endTimeField = new JTextField(15);
        endTimeField.setFont(UIStyles.LABEL_FONT);
        endTimeField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(endTimeField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = UIStyles.createAddButton("Add Time Slot");
        deleteButton = UIStyles.createDeleteButton("Delete Selected");
        refreshButton = UIStyles.createRefreshButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[] { "ID", "Day", "Start Time", "End Time" }, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        timeSlotTable = new JTable(tableModel);
        styleTable(timeSlotTable);
        
        JScrollPane scrollPane = new JScrollPane(timeSlotTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addTimeSlot());
        deleteButton.addActionListener(e -> deleteTimeSlot());
        refreshButton.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void styleTable(JTable table) {
        UIStyles.styleTableWithTimesNewRoman(table);
    }

    private void addTimeSlot() {
        String day = (String) dayCombo.getSelectedItem();
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (TimeSlotService.addTimeSlot(day, startTime, endTime)) {
            JOptionPane.showMessageDialog(this, "Time slot added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            startTimeField.setText("");
            endTimeField.setText("");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding time slot", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTimeSlot() {
        int selectedRow = timeSlotTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a time slot to delete", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (TimeSlotService.deleteTimeSlot(id)) {
            JOptionPane.showMessageDialog(this, "Time slot deleted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting time slot", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<TimeSlot> timeSlots = TimeSlotService.getAllTimeSlots();
            for (TimeSlot ts : timeSlots) {
                tableModel.addRow(new Object[] { ts.getId(), ts.getDay(), ts.getStartTime(), ts.getEndTime() });
            }
        } catch (Exception e) {
            // Database connection error - table will remain empty
        }
    }
}
