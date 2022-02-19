package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

//Represent visual user interface for image and current selection
public class ImageUI extends JPanel {

    private static final String IMAGE_FILE = "Smile.png"; //may end up becoming a list
    private static final String TITLE = "";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private Color fillColor;

    //EFFECTS: constructs the panel and displays photo
    public ImageUI() {

        this.setSize(WIDTH, HEIGHT);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(2,1));

        ImageIcon icon = new ImageIcon(IMAGE_FILE);

        JLabel label = new JLabel();
        label.setIcon(icon);
        panel.add(label);
        this.add(panel);

        this.setVisible(true);
    }

}
