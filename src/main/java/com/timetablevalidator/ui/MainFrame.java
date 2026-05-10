package com.timetablevalidator.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Timetable Constraint Validator");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(230, 240, 250));

        // Header Panel with gradient effect
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 118, 210), getWidth(), 0,
                        new Color(33, 150, 243));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Timetable Constraint Validator - Conflict Detection & Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Create tabbed pane with modern styling
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(new Color(240, 245, 250));
        tabbedPane.setForeground(new Color(25, 118, 210));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Add Conflict Report as first tab (main focus)
        tabbedPane.addTab("📊 Conflict Report", new ConflictReportPanel());
        
        // Add tabs for data management
        tabbedPane.addTab("👨‍🏫 Teachers", new TeacherInputPanel());
        tabbedPane.addTab("🏫 Rooms", new RoomInputPanel());
        tabbedPane.addTab("👥 Student Groups", new StudentGroupInputPanel());
        tabbedPane.addTab("⏰ Time Slots", new TimeSlotInputPanel());
        tabbedPane.addTab("📋 Assignments", new AssignmentInputPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom panel with info
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 245, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel infoLabel = new JLabel("✓ Add data in the tabs above, then use Conflict Report tab to validate and detect timetable conflicts");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(new Color(100, 100, 100));
        bottomPanel.add(infoLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}