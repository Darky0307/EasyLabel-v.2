package com.easylabel.ui;

import javax.swing.*;
import java.awt.*;

public class PaketversandPanel extends JPanel {
    private JRadioButton rbDhl, rbGls;
    private JTextField tfInvoice;
    private JButton btnPrint;
    private JProgressBar progressBar;

    public PaketversandPanel() {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Anzahl Pakete
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Anzahl Pakete:"), gbc);
        JTextField tfCount = new JTextField("1", 3);
        gbc.gridx = 1;
        add(tfCount, gbc);

        // Carrier Auswahl
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Carrier:"), gbc);
        JPanel carrierPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbDhl = new JRadioButton("DHL");
        rbGls = new JRadioButton("GLS");
        ButtonGroup group = new ButtonGroup();
        group.add(rbDhl);
        group.add(rbGls);
        rbDhl.setSelected(true);
        carrierPanel.add(rbDhl);
        carrierPanel.add(rbGls);
        gbc.gridx = 1;
        add(carrierPanel, gbc);

        // Lieferscheinnummer
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Lieferscheinnummer:"), gbc);
        tfInvoice = new JTextField(15);
        gbc.gridx = 1;
        add(tfInvoice, gbc);

        // Fortschrittsanzeige
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Fortschritt:"), gbc);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        gbc.gridx = 1;
        add(progressBar, gbc);

        // Druck-Button
        btnPrint = new JButton("Drucken");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPrint, gbc);

        btnPrint.addActionListener(e -> startPrint(tfCount.getText()));
    }

    private void startPrint(String countText) {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                int total = Integer.parseInt(countText);
                for (int i = 1; i <= total; i++) {
                    Thread.sleep(500);
                    publish((i * 100) / total);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int latest = chunks.get(chunks.size() - 1);
                progressBar.setValue(latest);
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(PaketversandPanel.this, "Druck abgeschlossen.");
            }
        };
        worker.execute();
    }
}
