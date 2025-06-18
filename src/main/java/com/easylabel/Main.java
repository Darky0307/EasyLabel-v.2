package com.easylabel;
import javax.swing.SwingUtilities;
import com.easylabel.ui.MainWindow;
public class Main {
    public static void main(String[] args) {
        // ensure UI created on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}