package Scenes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Howto extends JFrame {
    private ArrayList<ImageIcon> howtoImages;
    private JLabel imageLabel;
    private int currentImageIndex = 0;

    public Howto() {
        setTitle("How to Play");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#D5F2FF"));
        setLayout(new BorderLayout());

        loadImages();


        imageLabel = new JLabel(howtoImages.get(currentImageIndex));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#D5F2FF"));

        JButton nextButton = new JButton("다음으로");
        nextButton.addActionListener(e -> showNextImage());
        buttonPanel.add(nextButton);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadImages() {
        howtoImages = new ArrayList<>();
        howtoImages.add(new ImageIcon("assets/img/howtoplay1.png"));
        howtoImages.add(new ImageIcon("assets/img/howtoplay2.png"));
    }

    private void showNextImage() {
        currentImageIndex++;
        if (currentImageIndex >= howtoImages.size()) {
            currentImageIndex = 0;
        }
        imageLabel.setIcon(howtoImages.get(currentImageIndex));
    }

    public static void main(String[] args) {
        new Howto();
    }
}
