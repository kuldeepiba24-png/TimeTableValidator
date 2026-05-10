package com.timetablevalidator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.timetablevalidator.services.AssignmentService;
import com.timetablevalidator.model.Assignment;
import com.timetablevalidator.rules.*;
import java.util.List;
import java.util.ArrayList;

public class ConflictReportPanel extends JPanel {
    private JButton validateButton, exportButton;
    private JTable conflictTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public ConflictReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(new Color(255, 255, 255));
        controlPanel.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2, true));

        validateButton = UIStyles.createAddButton("Run Validation");
        exportButton = UIStyles.createRefreshButton("Export Report");

        controlPanel.add(validateButton);
        controlPanel.add(exportButton);

        // Status Label
        statusLabel = new JLabel("Click 'Run Validation' to check for conflicts", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Conflict Table
        tableModel = new DefaultTableModel(
            new String[]{"Conflict Type", "Description", "Severity"},
            0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        conflictTable = new JTable(tableModel);
        UIStyles.styleTableWithTimesNewRoman(conflictTable);
        conflictTable.setRowHeight(30);
        conflictTable.getColumnModel().getColumn(1).setPreferredWidth(400);

        // Set column renderers for better visualization
        conflictTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        conflictTable.getColumnModel().getColumn(2).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(conflictTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        // Summary Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(240, 245, 250));
        summaryPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel summaryLabel = new JLabel("Status: No validation run yet");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        summaryLabel.setForeground(UIStyles.PRIMARY_COLOR);
        summaryPanel.add(summaryLabel);

        add(summaryPanel, BorderLayout.SOUTH);

        // Action listeners
        validateButton.addActionListener(e -> runValidation());
        exportButton.addActionListener(e -> exportReport());
    }

    private void runValidation() {
        tableModel.setRowCount(0);
        List<String> allConflicts = new ArrayList<>();

        try {
            List<Assignment> assignments = AssignmentService.getAllAssignments();

            if (assignments.isEmpty()) {
                statusLabel.setText("No assignments found. Add assignments first.");
                statusLabel.setForeground(new Color(255, 100, 0));
                return;
            }

            // Run all validation rules
            ConstraintRule[] rules = {
                new RoomConflict(),
                new TeacherConflict(),
                new CapacityCheck()
            };

            for (ConstraintRule rule : rules) {
                List<String> conflicts = rule.validate(assignments);
                for (String conflict : conflicts) {
                    tableModel.addRow(new Object[]{
                        rule.getRuleName(),
                        conflict,
                        "High"
                    });
                    allConflicts.add(conflict);
                }
            }

            if (allConflicts.isEmpty()) {
                statusLabel.setText("✓ Validation Complete - No conflicts detected!");
                statusLabel.setForeground(new Color(76, 175, 80));
                tableModel.addRow(new Object[]{
                    "Status",
                    "All constraints satisfied - Timetable is valid",
                    "Low"
                });
            } else {
                statusLabel.setText("⚠ Validation Complete - " + allConflicts.size() + " conflict(s) found!");
                statusLabel.setForeground(new Color(255, 100, 0));
            }

        } catch (Exception e) {
            statusLabel.setText("Error running validation: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
            e.printStackTrace();
        }
    }

    private void exportReport() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please run validation first", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("===== TIMETABLE CONFLICT REPORT =====\n\n");

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            report.append(tableModel.getValueAt(i, 0)).append(" | ");
            report.append(tableModel.getValueAt(i, 1)).append(" | ");
            report.append(tableModel.getValueAt(i, 2)).append("\n");
        }

        JTextArea textArea = new JTextArea(10, 50);
        textArea.setText(report.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane, "Conflict Report", JOptionPane.INFORMATION_MESSAGE);
    }
}
