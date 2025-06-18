package com.easylabel.ui;

import com.easylabel.DhlApiClient;
import javax.swing.*;
import java.awt.*;

public class StornoPanel extends JPanel {
    private JTextField tfTracking;
    private JButton btnCancel;
    private DhlApiClient dhlClient;

    public StornoPanel(DhlApiClient dhlClient) {
        this.dhlClient = dhlClient;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel("Trackingnummer:"));
        tfTracking = new JTextField(20);
        add(tfTracking);
        btnCancel = new JButton("Stornieren");
        add(btnCancel);
        btnCancel.addActionListener(e -> {
            try {
                boolean ok = dhlClient.cancelShipment(tfTracking.getText());
                JOptionPane.showMessageDialog(this, ok ? "Storno erfolgreich" : "Fehler beim Storno");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage());
            }
        });
    }
}