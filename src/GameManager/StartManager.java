package GameManager;

import Scenes.Play;

import javax.swing.*;
import java.awt.*;

public class StartManager extends JFrame {
    private int imageWidth = 200;
    private int imageHeight = 75;
    private final int targetWidth = 800;
    private final int targetHeight = 300;
    private final int animationSpeed = 20;
    private final int growthStep = 20;
    private int[] coins = {5, 10, 20, 50, 75, 90, 100};
    private JLabel imageLabel;
    private JLabel textLabel;
    private JPanel contentPanel;
    private Timer animationTimer;
    private DayManager dayManager;

    public StartManager() {
        this.dayManager = new DayManager();

        setTitle("Start Manager");
        setSize(1038, 805);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // setLocationRelativeTo(null); --> 메인이랑 위치 맞출라구 주석 처리 했어여
        setLayout(null);

        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.decode("#E3F6FF"));
        contentPanel.setBounds(0, 0, 1024, 768);
        add(contentPanel);

        ImageIcon originalIcon = new ImageIcon("assets/img/GameStart.png");
        imageLabel = new JLabel(originalIcon);
        updateImageSize();

        contentPanel.add(imageLabel);

        textLabel = new JLabel("", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 150));
        textLabel.setForeground(Color.BLACK);
        textLabel.setVisible(false);
        contentPanel.add(textLabel);

        setVisible(true);
        startAnimation();
    }

    private void startAnimation() {
        animationTimer = new Timer(animationSpeed, e -> {
            if (imageWidth < targetWidth || imageHeight < targetHeight) {
                imageWidth = Math.min(imageWidth + growthStep, targetWidth);
                imageHeight = Math.min(imageHeight + growthStep * 3 / 4, targetHeight);
                updateImageSize();
            } else {
                animationTimer.stop();
                showSecondStage();
            }
        });
        animationTimer.start();
    }

    private void showSecondStage() {
        Timer secondStageTimer = new Timer(2000, e -> {
            ((Timer) e.getSource()).stop();

            ImageIcon secondIcon = new ImageIcon("assets/img/coinGoal.png");
            Image scaledImage = secondIcon.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));

            imageLabel.setBounds(262, 150, 500, 200);

            int day = dayManager.getDay();
            int coinValue = (day > 0 && day <= coins.length) ? coins[day - 1] : 0;
            textLabel.setText(String.valueOf(coinValue));
            textLabel.setBounds(0, 320, 1024, 120);
            textLabel.setVisible(true);
            textLabel.setForeground(Color.decode("#E9DB47"));

            Timer playTimer = new Timer(2000, event -> {
                dispose();
                new Play();
            });
            playTimer.setRepeats(false);
            playTimer.start();
        });
        secondStageTimer.setRepeats(false);
        secondStageTimer.start();
    }

    private void updateImageSize() {
        ImageIcon originalIcon = new ImageIcon("assets/img/GameStart.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));

        int x = (1024 - imageWidth) / 2;
        int y = (768 - imageHeight) / 2;
        imageLabel.setBounds(x, y, imageWidth, imageHeight);
    }

    public static void main(String[] args) {
        new StartManager();
    }
}
