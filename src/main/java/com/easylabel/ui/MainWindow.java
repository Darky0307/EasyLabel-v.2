package com.easylabel.ui;

import com.easylabel.PlentyApiClient;
import com.easylabel.DhlApiClient;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("EasyLabel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---- Config laden ----
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            // Falls config fehlt, werden Defaults genutzt
        }

        // ---- API-Clients initialisieren ----
        PlentyApiClient plentyClient = new PlentyApiClient(
            props.getProperty("plenty.url"),
            props.getProperty("plenty.user"),
            props.getProperty("plenty.pass")
        );
        DhlApiClient dhlClient = new DhlApiClient(
            props.getProperty("dhl.url"),
            props.getProperty("dhl.pass")
        );

        // ---- Tabs bauen ----
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Paketversand", new PaketversandPanel());
        tabs.addTab("Manueller Druck", new ManuellerDruckPanel(plentyClient, dhlClient));
        tabs.addTab("Storno DHL",     new StornoPanel(dhlClient));
        tabs.addTab("Einstellungen", new SettingsPanel(props));

        setJMenuBar(createMenuBar());
        add(tabs, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Info"));
        menuBar.add(new JMenu("Einstellungen"));
        return menuBar;
    }
}
