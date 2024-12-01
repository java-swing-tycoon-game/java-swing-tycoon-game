package GameManager;

import Scenes.Play;

import javax.swing.*;
import java.awt.*;

public class StartManager extends JDialog {
    private int imageWidth = 200;
    private int imageHeight = 75;
    private final int targetWidth = 800;
    private final int targetHeight = 300;
    private final int animationSpeed = 20;
    private final int growthStep = 20;
    private int[] coins = {5, 10, 20, 50, 75, 90, 100};
    private JLabel imageLabel;
    private JPanel contentPanel;
    private Timer animationTimer;
    private DayManager dayManager;
    private boolean isFirstDay; // 첫날 여부 플래그

    public StartManager(DayManager dayManager, boolean isFirstDay) {
        this.dayManager = dayManager;
        this.isFirstDay = isFirstDay;

        setUndecorated(true);
        setSize(1038, 805);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setupContentPanel();

        if (isFirstDay) {
            setupImageLabel("assets/img/GameStart.png");
        }

        setVisible(true);
        startAnimation();
    }

    private void startAnimation() {
        if(dayManager.getDay() >7)return;
        if (isFirstDay) {
            // 첫날: GameStart.png 애니메이션 실행
            animationTimer = new Timer(animationSpeed, e -> {
                if (imageWidth < targetWidth || imageHeight < targetHeight) {
                    imageWidth = Math.min(imageWidth + growthStep, targetWidth);
                    imageHeight = Math.min(imageHeight + growthStep * 3 / 4, targetHeight);
                    updateImageSize();
                } else {
                    animationTimer.stop();
                    showCoinGoalStage();
                }
            });
            animationTimer.start();

        } else {
            showCoinGoalStage();

        }
    }

    private void showCoinGoalStage() {
        contentPanel.removeAll();
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 1024, 768);

        // coinGoal.png 배경 설정
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon("assets/img/coinGoal.png").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 1024, 768);
        setContentPane(contentPanel);

        int day = dayManager.getDay();
        int coinValue = (day > 0 && day <= coins.length) ? coins[day - 1] : 0;


        JLabel growingTextLabel = new JLabel(String.valueOf(coinValue), SwingConstants.CENTER);
        growingTextLabel.setFont(FontManager.loadFont(50f));
        growingTextLabel.setForeground(Color.decode("#FD3D39"));
        growingTextLabel.setBounds(350, 400, 350, 300);
        contentPanel.add(growingTextLabel);


        Timer growTimer = new Timer(20, e -> {
            Font currentFont = growingTextLabel.getFont();
            float newSize = currentFont.getSize2D() + 3;
            if (newSize < 180f) {
                growingTextLabel.setFont(currentFont.deriveFont(newSize));
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        growTimer.start();

        Timer nextStageTimer = new Timer(1500, e -> {
            if (isFirstDay) {

                Timer playTimer = new Timer(500, playEvent -> {
                    new Play();
                    dispose();

                });
                playTimer.setRepeats(false);
                playTimer.start();
            } else {

                Timer resumeTimer = new Timer(500, resumeEvent -> {
                    dispose();
                });
                resumeTimer.setRepeats(false);
                resumeTimer.start();
            }
        });
        nextStageTimer.setRepeats(false);
        nextStageTimer.start();
    }



    private void setupContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.decode("#FED9FC"));
        contentPanel.setBounds(0, 0, 1024, 768);
        add(contentPanel);
    }

    private void setupImageLabel(String imagePath) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        imageLabel = new JLabel(originalIcon);
        updateImageSize();
        contentPanel.add(imageLabel);
    }

    private void updateImageSize() {
        if (imageLabel == null) return;

        ImageIcon originalIcon = new ImageIcon("assets/img/GameStart.png");

        Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));

        int x = (1024 - imageWidth) / 2;
        int y = (768 - imageHeight) / 2;
        imageLabel.setBounds(x, y, imageWidth, imageHeight);
    }



    public static void main(String[] args) {
        DayManager dayManager = DayManager.getInstance();

        new StartManager(dayManager, true);
        new StartManager(dayManager, false);
    }
}
