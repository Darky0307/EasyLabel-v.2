package com.easylabel.ui;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class SettingsPanel extends JPanel {
    private JComboBox<String> printerCombo;
    private JTextField plentyUrlField, userField;
    private JPasswordField passField;
    private JTextField dhlUrlField, dhlUserField;
    private JPasswordField dhlPassField;
    private JButton btnSave;

    public SettingsPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        // Drucker Auswahl
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Drucker:"), gbc);
        printerCombo = new JComboBox<>(getInstalledPrinters());
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(printerCombo, gbc);

        // PlentyMarkets URL
        y++; gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        add(new JLabel("PlentyMarkets API-URL:"), gbc);
        plentyUrlField = new JTextField(30);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(plentyUrlField, gbc);

        // PlentyMarkets User
        y++; gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Benutzer:"), gbc);
        userField = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(userField, gbc);

        // PlentyMarkets Passwort + Toggle Eye
        y++; gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Passwort:"), gbc);
        passField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridwidth = 1;
        add(passField, gbc);
        JToggleButton togglePass = new JToggleButton("👁");
        togglePass.setPreferredSize(new Dimension(30, passField.getPreferredSize().height));
        gbc.gridx = 2;
        add(togglePass, gbc);
        togglePass.addActionListener(e -> {
            passField.setEchoChar(togglePass.isSelected() ? (char)0 : '*');
        });

        // DHL URL
        y++; gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        add(new JLabel("DHL API-URL:"), gbc);
        dhlUrlField = new JTextField(30);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(dhlUrlField, gbc);

        // DHL User
        y++; gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("DHL Benutzer:"), gbc);
        dhlUserField = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(dhlUserField, gbc);

        // DHL Passwort + Toggle Eye
        y++; gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("DHL Passwort:"), gbc);
        dhlPassField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridwidth = 1;
        add(dhlPassField, gbc);
        JToggleButton toggleDhlPass = new JToggleButton("👁");
        toggleDhlPass.setPreferredSize(new Dimension(30, dhlPassField.getPreferredSize().height));
        gbc.gridx = 2;
        add(toggleDhlPass, gbc);
        toggleDhlPass.addActionListener(e -> {
            dhlPassField.setEchoChar(toggleDhlPass.isSelected() ? (char)0 : '*');
        });

        // Speichern-Button
        y++; gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 3;
        btnSave = new JButton("Speichern");
        add(btnSave, gbc);
        btnSave.addActionListener(e -> saveSettings());
    }

    public SettingsPanel(Properties props) {
        this();
        printerCombo.setSelectedItem(props.getProperty("printer", ""));
        plentyUrlField.setText(props.getProperty("plenty.url", ""));
        userField.setText(props.getProperty("plenty.user", ""));
        passField.setText(props.getProperty("plenty.pass", ""));
        dhlUrlField.setText(props.getProperty("dhl.url", ""));
        dhlUserField.setText(props.getProperty("dhl.user", ""));
        dhlPassField.setText(props.getProperty("dhl.pass", ""));
    }

    private void saveSettings() {
        Properties props = new Properties();
        props.setProperty("printer", (String)printerCombo.getSelectedItem());
        props.setProperty("plenty.url", plentyUrlField.getText());
        props.setProperty("plenty.user", userField.getText());
        props.setProperty("plenty.pass", new String(passField.getPassword()));
        props.setProperty("dhl.url", dhlUrlField.getText());
        props.setProperty("dhl.user", dhlUserField.getText());
        props.setProperty("dhl.pass", new String(dhlPassField.getPassword()));
        try (FileOutputStream out = new FileOutputStream("config.properties")) {
            props.store(out, "EasyLabel Configuration");
            JOptionPane.showMessageDialog(this, "Einstellungen gespeichert.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern: " + ex.getMessage());
        }
    }

    private String[] getInstalledPrinters() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String[] names = new String[services.length];
        for (int i = 0; i < services.length; i++) {
            names[i] = services[i].getName();
        }
        return names;
    }
}
