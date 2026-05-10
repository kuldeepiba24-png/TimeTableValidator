package com.timetablevalidator.ui;

import javax.swing.*;
import java.awt.*;

public class UIStyles {

    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    public static final Color SECONDARY_COLOR = new Color(33, 150, 243);
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    public static final Color DANGER_COLOR = new Color(244, 67, 54);
    public static final Color WARNING_COLOR = new Color(255, 152, 0);
    public static final Color BACKGROUND_COLOR = new Color(240, 245, 250);
    public static final Color LIGHT_GRAY = new Color(245, 245, 245);
    public static final Color DARK_TEXT = new Color(33, 33, 33);

    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font TABLE_HEADER_FONT = new Font("Times New Roman", Font.BOLD, 13);
    public static final Font TABLE_CONTENT_FONT = new Font("Times New Roman", Font.PLAIN, 12);
    public static final Color TABLE_TEXT_COLOR = new Color(25, 118, 210);

    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }

    public static JButton createAddButton(String text) {
        return createStyledButton(text, SUCCESS_COLOR);
    }

    public static JButton createDeleteButton(String text) {
        return createStyledButton(text, DANGER_COLOR);
    }

    public static JButton createRefreshButton(String text) {
        return createStyledButton(text, PRIMARY_COLOR);
    }

    public static void styleInputPanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220), 2, true));
    }

    public static void styleTitledBorder(JPanel panel, String title) {
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                HEADER_FONT,
                PRIMARY_COLOR));
    }

    public static void styleTableWithTimesNewRoman(JTable table) {
        table.setFont(TABLE_CONTENT_FONT);
        table.setForeground(TABLE_TEXT_COLOR);
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(TABLE_HEADER_FONT);
        
        // Custom cell renderer for professional appearance
        javax.swing.table.DefaultTableCellRenderer renderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, 
                                                          boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                c.setFont(TABLE_CONTENT_FONT);
                if (!isSelected) {
                    c.setForeground(TABLE_TEXT_COLOR);
                    c.setBackground(row % 2 == 0 ? Color.WHITE : LIGHT_GRAY);
                } else {
                    c.setForeground(Color.WHITE);
                    c.setBackground(SECONDARY_COLOR);
                }
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
}
