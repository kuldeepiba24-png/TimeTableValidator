package com.timetablevalidator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import com.timetablevalidator.services.RoomService;
import com.timetablevalidator.model.Room;
import java.util.List;

public class RoomInputPanel extends JPanel {
    private JTextField roomNumberField, capacityField;
    private JButton addButton, deleteButton, refreshButton;
    private JTable roomTable;
    private DefaultTableModel tableModel;

    public RoomInputPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        UIStyles.styleTitledBorder(inputPanel, "Add New Room");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomLabel = new JLabel("Room Number:");
        roomLabel.setFont(UIStyles.LABEL_FONT);
        roomLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(roomLabel, gbc);
        gbc.gridx = 1;
        roomNumberField = new JTextField(15);
        roomNumberField.setFont(UIStyles.LABEL_FONT);
        roomNumberField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(roomNumberField, gbc);

        JLabel capLabel = new JLabel("Capacity:");
        capLabel.setFont(UIStyles.LABEL_FONT);
        capLabel.setForeground(UIStyles.DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(capLabel, gbc);
        gbc.gridx = 1;
        capacityField = new JTextField(15);
        capacityField.setFont(UIStyles.LABEL_FONT);
        capacityField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        inputPanel.add(capacityField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = UIStyles.createAddButton("Add Room");
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

        tableModel = new DefaultTableModel(new String[]{"ID", "Room Number", "Capacity"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        roomTable = new JTable(tableModel);
        styleTable(roomTable);
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.PRIMARY_COLOR, 2));
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        refreshButton.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void styleTable(JTable table) {
        UIStyles.styleTableWithTimesNewRoman(table);
    }

    private void addRoom() {
        String roomNumber = roomNumberField.getText().trim();
        String capacityStr = capacityField.getText().trim();

        if (roomNumber.isEmpty() || capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int capacity = Integer.parseInt(capacityStr);
            if (RoomService.addRoom(roomNumber, capacity)) {
                JOptionPane.showMessageDialog(this, "Room added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                roomNumberField.setText("");
                capacityField.setText("");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding room", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (RoomService.deleteRoom(id)) {
            JOptionPane.showMessageDialog(this, "Room deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting room", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Room> rooms = RoomService.getAllRooms();
            for (Room room : rooms) {
                tableModel.addRow(new Object[]{room.getId(), room.getRoomNumber(), room.getCapacity()});
            }
        } catch (Exception e) {
            // Database connection error - table will remain empty
        }
    }
}
