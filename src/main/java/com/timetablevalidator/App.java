package com.timetablevalidator;

import javax.swing.SwingUtilities;
import com.timetablevalidator.ui.MainFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}