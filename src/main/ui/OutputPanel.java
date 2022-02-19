package ui;

import model.Catalogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;

//represent visual output of search and view catalogues
public class OutputPanel extends JInternalFrame {
    private static final int WIDTH = LibraryAppUI.WIDTH;
    private static final int HEIGHT = LibraryAppUI.HEIGHT;
    private static final int TEXT_INDENT = 30;

    private List<String> wholeCatalogue;
    private Color fillColor;
    private JTextArea textArea;

    public OutputPanel() {
        super("Results:");
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        setSize(WIDTH / 2, HEIGHT - 20);
        setLocation(WIDTH / 2, HEIGHT / 15);

        textArea.setText("Welcome, add books, search titles and view the catalogues...\n\n");

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: prints all the list of titles given
    public void printCatalogue(List<String> strings) {
        this.wholeCatalogue = strings;

        if (wholeCatalogue.isEmpty()) {
            textArea.setText("No titles to view...\n\n");
        } else {
            textArea.setText("");
            for (String s: wholeCatalogue) {
                textArea.setText(textArea.getText() + s + "\n\n");
            }
        }

        repaint();
    }

}
