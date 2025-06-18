package com.easylabel.ui;

import com.easylabel.PlentyApiClient;
import com.easylabel.DhlApiClient;
import com.easylabel.model.OrderData;
import com.easylabel.model.AddressData;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Panel für den manuellen Label-Druck.
 */
public class ManuellerDruckPanel extends JPanel {
    private final PlentyApiClient plentyClient;
    private final DhlApiClient dhlClient;

    private JTextField tfInvoice, tfRef, tfName1, tfName2, tfStreet, tfPostal, tfCity;
    private JComboBox<String> cbCountry;
    private JRadioButton rbDhl, rbGls;
    private JButton btnLoad, btnPrint;
    private JProgressBar progressBar;

    public ManuellerDruckPanel(PlentyApiClient plentyClient, DhlApiClient dhlClient) {
        this.plentyClient = plentyClient;
        this.dhlClient = dhlClient;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Lieferschein Nr.
        addLabel("Lieferschein Nr.:", 0, row, gbc);
        tfInvoice = new JTextField(12);
        add(tfInvoice, cell(1, row, gbc));
        btnLoad = new JButton("Laden");
        add(btnLoad, cell(2, row, gbc));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, cell(3, row, gbc));

        // Referenz
        row++;
        addLabel("Referenz:", 0, row, gbc);
        tfRef = new JTextField(20);
        add(tfRef, cell(1, row, 3, gbc));

        // Name 1
        row++;
        addLabel("Name 1*:", 0, row, gbc);
        tfName1 = new JTextField(20);
        add(tfName1, cell(1, row, 3, gbc));

        // Name 2
        row++;
        addLabel("Name 2:", 0, row, gbc);
        tfName2 = new JTextField(20);
        add(tfName2, cell(1, row, 3, gbc));

        // Straße
        row++;
        addLabel("Straße*:", 0, row, gbc);
        tfStreet = new JTextField(20);
        add(tfStreet, cell(1, row, 3, gbc));

        // PLZ und Ort
        row++;
        addLabel("PLZ*:", 0, row, gbc);
        tfPostal = new JTextField(6);
        add(tfPostal, cell(1, row, gbc));
        addLabel("Ort*:", 2, row, gbc);
        tfCity = new JTextField(12);
        add(tfCity, cell(3, row, gbc));

        // Land
        row++;
        addLabel("Land*:", 0, row, gbc);
        cbCountry = createCountryCombo();
        add(cbCountry, cell(1, row, 3, gbc));

        // Carrier-Auswahl
        row++;
        addLabel("Carrier:", 0, row, gbc);
        rbDhl = new JRadioButton("DHL");
        rbGls = new JRadioButton("GLS");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbDhl);
        bg.add(rbGls);
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnl.add(rbDhl);
        pnl.add(rbGls);
        rbDhl.setSelected(true);
        add(pnl, cell(1, row, 3, gbc));

        // Drucken-Button
        row++;
        btnPrint = new JButton("Drucken");
        add(btnPrint, cell(0, row, 4, gbc));

        // Listener
        btnLoad.addActionListener(e -> loadData());
        btnPrint.addActionListener(e -> printShipment());
    }

    private void addLabel(String text, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(new JLabel(text), gbc);
    }

    private GridBagConstraints cell(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        return gbc;
    }

    private GridBagConstraints cell(int x, int y, int w, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        return gbc;
    }

    private JComboBox<String> createCountryCombo() {
        JComboBox<String> combo = new JComboBox<>();
        // Alle Länder aus statischer Map
        for (String country : PlentyApiClient.PLE_COUNTRY_MAP.values()) {
            combo.addItem(country);
        }
        return combo;
    }

    private void loadData() {
        btnLoad.setEnabled(false);
        progressBar.setValue(0);
        SwingWorker<OrderData, Void> worker = new SwingWorker<OrderData, Void>() {
            @Override
            protected OrderData doInBackground() throws Exception {
                return plentyClient.loadOrder(tfInvoice.getText());
            }

            @Override
            
            protected void done() {
                btnLoad.setEnabled(true);
                try {
                    OrderData data = get();
                    tfRef.setText(data.getInvoiceNumber());
                    tfName1.setText(data.getRecipientName());
                    tfName2.setText("");
                    tfStreet.setText(data.getStreet());
                    tfPostal.setText(data.getPostalCode());
                    tfCity.setText(data.getCity());

                    // Debug-Ausgabe für Country-Auswahl
                    String toSelect = data.getCountryName() != null ? data.getCountryName().trim() : "";
                    System.out.println("[DEBUG] countryName geladen: '" + toSelect + "'");
                    System.out.println("[DEBUG] ComboBox-Inhalte:");
                    for (int i = 0; i < cbCountry.getItemCount(); i++) {
                        System.out.println("  [" + i + "] '" + cbCountry.getItemAt(i) + "'");
                    }

                    // Erstversuch: Standard-Methode
                    cbCountry.setSelectedItem(toSelect);

                    // Fallback: case-insensitive Loop
                    Object selected = cbCountry.getSelectedItem();
                    if (selected == null || !toSelect.equals(selected)) {
                        for (int i = 0; i < cbCountry.getItemCount(); i++) {
                            String item = cbCountry.getItemAt(i);
                            if (item.equalsIgnoreCase(toSelect)) {
                                cbCountry.setSelectedIndex(i);
                                System.out.println("[DEBUG] Land per Fallback auf Index " + i + " gesetzt.");
                                break;
                            }
                        }
                    }

                    progressBar.setValue(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ee) {
                    String msg = ee.getCause() != null ? ee.getCause().getMessage() : ee.getMessage();
                    JOptionPane.showMessageDialog(
                        ManuellerDruckPanel.this,
                        "Fehler beim Laden: " + msg,
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        worker.execute();
    }

    private void printShipment() {
        btnPrint.setEnabled(false);
        progressBar.setValue(0);
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Beispiel: Labeldruck mit dhlClient
                // ShipmentRequest req = new ShipmentRequest(...);
                // dhlClient.createShipment(req);
                publish(100);
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                progressBar.setValue(chunks.get(chunks.size() - 1));
            }

            @Override
            protected void done() {
                btnPrint.setEnabled(true);
                try {
                    get();
                    JOptionPane.showMessageDialog(
                            ManuellerDruckPanel.this,
                            "Etikett erfolgreich gedruckt!",
                            "Erfolg",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ee) {
                    String msg = ee.getCause() != null ? ee.getCause().getMessage() : ee.getMessage();
                    JOptionPane.showMessageDialog(
                            ManuellerDruckPanel.this,
                            "Fehler beim Drucken: " + msg,
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        worker.execute();
    }
}
