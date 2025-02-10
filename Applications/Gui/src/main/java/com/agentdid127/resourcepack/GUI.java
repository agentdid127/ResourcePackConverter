package com.agentdid127.resourcepack;

import com.agentdid127.resourcepack.backwards.BackwardsPackConverter;
import com.agentdid127.resourcepack.forwards.ForwardsPackConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class GUI extends JPanel {
    private PrintStream out;
    private JTextArea outputLogPane;
    private JComboBox<String> initialVersionBox;
    private JComboBox<String> targetVersions;
    private JCheckBox minifyCheckBox;
    private JComboBox<String> lightOptions;
    private JButton convertButton;

    public GUI() {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setStrictness(Strictness.LENIENT);
        Gson gson = gsonBuilder.disableHtmlEscaping().create();
        setupGUI(gson);
        Arrays.stream((new String[]{"none", "front", "side"})).forEach(lightOptions::addItem);
        convertButton.addActionListener(e -> {
            out = redirectSystemStreams();
            String light = Objects.requireNonNull(lightOptions.getSelectedItem()).toString();
            int from = Util.getVersionProtocol(gson, Objects.requireNonNull(initialVersionBox.getSelectedItem()).toString());
            int to = Util.getVersionProtocol(gson, Objects.requireNonNull(targetVersions.getSelectedItem()).toString());
            boolean minify = minifyCheckBox.isSelected();
            new Thread(() -> {
                convertButton.setEnabled(false);
                try {
                    Gson packGson = gson;
                    if (!minify) {
                        packGson = gson.newBuilder().setPrettyPrinting().create();
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
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RPC - Resource Pack Converter");
        frame.setContentPane(new GUI());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new GTKLookAndFeel());
        } catch (Exception ignored) {
            System.err.println("GTK look not supported, ignoring.");
        }

        frame.setFocusable(true);
        Dimension dimensions = new Dimension(854, 480);
        frame.setMaximumSize(dimensions);
        frame.setMinimumSize(dimensions);
        frame.setPreferredSize(dimensions);
        frame.setLocationRelativeTo(null); // Centers window on screen
        frame.pack();
        frame.setVisible(true);
    }

    private void setupGUI(Gson gson) {
        {
            final JPanel panel = new JPanel();
            final JScrollPane scrollPane = new JScrollPane();
            this.outputLogPane = new JTextArea();
            this.outputLogPane.setColumns(60);
            this.outputLogPane.setRows(20);
            this.outputLogPane.setText("");
            this.outputLogPane.setEditable(false);
            scrollPane.setViewportView(this.outputLogPane);
            panel.add(scrollPane);
            this.add(panel);
        }

        {
            final JPanel menuBar = new JPanel();
            // Initial Version
            JLabel initialVersionLabel = new JLabel();
            initialVersionLabel.setText("Initial Version");
            menuBar.add(initialVersionLabel);

            this.initialVersionBox = new JComboBox<>();
            initialVersionLabel.setLabelFor(this.initialVersionBox);
            menuBar.add(this.initialVersionBox);

            // Target Version
            JLabel targetVersionsLabel = new JLabel();
            targetVersionsLabel.setText("Final Version");
            menuBar.add(targetVersionsLabel);

            this.targetVersions = new JComboBox<>();
            targetVersionsLabel.setLabelFor(targetVersions);
            menuBar.add(this.targetVersions);

            // Add items to both ^
            String[] versions = Util.getSupportedVersions(gson);
            if (versions == null) {
                throw new RuntimeException("Failed to get supported version, application possibly corrupt!");
            }

            if (versions.length > 0) {
                for (String version : versions) {
                    initialVersionBox.addItem(version);
                    targetVersions.addItem(version);
                }
                targetVersions.setSelectedIndex(targetVersions.getItemCount() - 1);
            }

            // Minify Checkbox
            JLabel minifyCheckboxLabel = new JLabel();
            minifyCheckboxLabel.setText("Minify");
            menuBar.add(minifyCheckboxLabel);

            this.minifyCheckBox = new JCheckBox();
            minifyCheckboxLabel.setLabelFor(this.minifyCheckBox);
            menuBar.add(this.minifyCheckBox);

            // Item Lighting Options
            JLabel lightOptionsLabel = new JLabel();
            lightOptionsLabel.setText("Item Lighting");
            menuBar.add(lightOptionsLabel);

            this.lightOptions = new JComboBox<>();
            lightOptionsLabel.setLabelFor(this.lightOptions);
            menuBar.add(this.lightOptions);

            // Convert Button
            this.convertButton = new JButton();
            this.convertButton.setText("Convert");
            menuBar.add(this.convertButton);

            this.add(menuBar);
        }
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
