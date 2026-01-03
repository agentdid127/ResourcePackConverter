package com.agentdid127.resourcepack;

import com.agentdid127.resourcepack.backwards.BackwardsPackConverter;
import com.agentdid127.resourcepack.forwards.ForwardsPackConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class GUI extends JPanel {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().disableHtmlEscaping().create();

    private PrintStream out;
    private final JTextArea outputLogPane;
    private final JComboBox<String> baseVersions;
    private final JComboBox<String> targetVersions;
    private final JCheckBox minifyCheckBox;
    private final JComboBox<String> lightOptions;
    private final JButton convertButton;

    public GUI() {
        // Log Output Panel
        final JPanel logPanel = new JPanel();
        final JScrollPane scrollPane = new JScrollPane();
        this.outputLogPane = new JTextArea();
        this.outputLogPane.setColumns(60);
        this.outputLogPane.setRows(20);
        this.outputLogPane.setText("");
        this.outputLogPane.setEditable(false);
        ((DefaultCaret) this.outputLogPane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPane.setViewportView(this.outputLogPane);
        logPanel.add(scrollPane);
        this.add(logPanel);

        // Menubar Panel
        final JPanel menuBarPanel = new JPanel();
        // Base Version
        JLabel baseVersionLabel = new JLabel();
        baseVersionLabel.setText("Base Version");
        menuBarPanel.add(baseVersionLabel);
        this.baseVersions = new JComboBox<>();
        baseVersionLabel.setLabelFor(this.baseVersions);
        menuBarPanel.add(this.baseVersions);

        // Target Version
        JLabel targetVersionsLabel = new JLabel();
        targetVersionsLabel.setText("Target Version");
        menuBarPanel.add(targetVersionsLabel);
        this.targetVersions = new JComboBox<>();
        targetVersionsLabel.setLabelFor(targetVersions);
        menuBarPanel.add(this.targetVersions);

        // Add items to both ^
        String[] versions = Util.getSupportedVersions(GSON);
        if (versions == null) {
            throw new RuntimeException("Failed to get supported version, application possibly corrupt!");
        } else if (versions.length > 0) {
            for (String version : versions) {
                baseVersions.addItem(version);
                targetVersions.addItem(version);
            }

            targetVersions.setSelectedIndex(targetVersions.getItemCount() - 1);
        }

        // Minify Checkbox
        JLabel minifyCheckboxLabel = new JLabel();
        minifyCheckboxLabel.setText("Minify");
        menuBarPanel.add(minifyCheckboxLabel);

        this.minifyCheckBox = new JCheckBox();
        minifyCheckboxLabel.setLabelFor(this.minifyCheckBox);
        menuBarPanel.add(this.minifyCheckBox);

        // Item Lighting Options
        JLabel lightOptionsLabel = new JLabel();
        lightOptionsLabel.setText("Item Lighting");
        menuBarPanel.add(lightOptionsLabel);

        this.lightOptions = new JComboBox<>();
        lightOptionsLabel.setLabelFor(this.lightOptions);
        menuBarPanel.add(this.lightOptions);

        Arrays.stream((new String[]{"none", "front", "side"})).forEach(lightOptions::addItem);

        // Convert Button
        this.convertButton = new JButton();
        this.convertButton.setText("Convert");
        this.convertButton.addActionListener(e -> {
            out = redirectSystemStreams();
            String light = Objects.requireNonNull(lightOptions.getSelectedItem()).toString();
            int from = Util.getVersionProtocol(GSON, Objects.requireNonNull(baseVersions.getSelectedItem()).toString());
            int to = Util.getVersionProtocol(GSON, Objects.requireNonNull(targetVersions.getSelectedItem()).toString());
            boolean minify = minifyCheckBox.isSelected();
            new Thread(() -> {
                convertButton.setEnabled(false);
                try {
                    Gson packGson = GSON;
                    if (!minify) {
                        packGson = packGson.newBuilder().setPrettyPrinting().create();
                    }

                    Path dotPath = Paths.get("./");
                    if (from < to) {
                        new ForwardsPackConverter(packGson, from, to, light, dotPath, true, out).runDir();
                    } else {
                        new BackwardsPackConverter(packGson, from, to, dotPath, true, out).runDir();
                    }
                } catch (Exception exception) {
                    out.println(Arrays.toString(exception.getStackTrace()));
                }
                convertButton.setEnabled(true);
            }).start();
        });
        menuBarPanel.add(this.convertButton);

        this.add(menuBarPanel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RPC - Resource Pack Converter");
        frame.setContentPane(new GUI());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception ignored) {
            System.err.println("GTK look not supported, ignoring.");
        }

        Dimension dimensions = new Dimension(854, 480);
        frame.setMinimumSize(dimensions);
        frame.setPreferredSize(dimensions);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Centers window on screen
        frame.pack();
        frame.setVisible(true);
    }

    private PrintStream redirectSystemStreams() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                outputLogPane.append(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                outputLogPane.append(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) {
                write(b, 0, b.length);
            }
        });
    }
}
