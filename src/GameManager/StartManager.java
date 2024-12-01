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
    private JPanel contentPanel;
    private Timer animationTimer;
    private DayManager dayManager;
    private boolean isFirstDay; // 첫날 여부 플래그

    public StartManager(DayManager dayManager, boolean isFirstDay) {
        this.dayManager = dayManager;
        this.isFirstDay = isFirstDay;

        setTitle("Start Manager");
        setSize(1038, 805);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupContentPanel();

        if (isFirstDay) {
            setupImageLabel("assets/img/GameStart.png");
        }

        setVisible(true);
        startAnimation();
    }

    private void startAnimation() {
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
            // 둘째 날 이후 바로 coinGoal.png 실행
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

        // Coin 목표값 회전 텍스트
        RotatingTextLabel rotatingTextLabel = new RotatingTextLabel(String.valueOf(coinValue));
        rotatingTextLabel.setBounds(0, 200, 1024, 768);
        contentPanel.add(rotatingTextLabel);

        Timer rotationTimer = new Timer(5, rotationEvent -> {
            rotatingTextLabel.setAngle((rotatingTextLabel.getAngle() + 20) % 360);
        });
        rotationTimer.start();

        Timer stopRotationTimer = new Timer(1500, stopEvent -> {
            rotationTimer.stop();
            rotatingTextLabel.setAngle(0);

            if (isFirstDay) {
                // 첫날: Play 시작
                Timer playTimer = new Timer(500, playEvent -> {
                    dispose();
                    new Play();
                });
                playTimer.setRepeats(false);
                playTimer.start();
            } else {
                // 둘째 날 이후: 기존 게임 상태 유지
                Timer resumeTimer = new Timer(500, resumeEvent -> {
                    dispose(); // 창 닫고 기존 게임으로 돌아감
                });
                resumeTimer.setRepeats(false);
                resumeTimer.start();
            }
        });
        stopRotationTimer.setRepeats(false);
        stopRotationTimer.start();
    }

    private void setupContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.decode("#E3F6FF"));
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

        // ImageIcon originalIcon = (ImageIcon) imageLabel.getIcon();
        ImageIcon originalIcon = new ImageIcon("assets/img/GameStart.png");

        Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));

        int x = (1024 - imageWidth) / 2;
        int y = (768 - imageHeight) / 2;
        imageLabel.setBounds(x, y, imageWidth, imageHeight);
    }

    class RotatingTextLabel extends JLabel {
        private double angle = 0;

        public RotatingTextLabel(String text) {
            super(text, SwingConstants.CENTER);
            setFont(FontManager.loadFont(150f));
            setForeground(Color.decode("#FD3D39"));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            g2d.setFont(getFont());
            g2d.setColor(getForeground());
            g2d.translate(centerX, centerY);
            g2d.rotate(Math.toRadians(angle));
            g2d.translate(-centerX, -centerY);
            g2d.drawString(getText(), centerX - g2d.getFontMetrics().stringWidth(getText()) / 2, centerY);
            g2d.dispose();
        }

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
            repaint();
        }
    }

    public static void main(String[] args) {
        DayManager dayManager = DayManager.getInstance();

        // Day 1 시작
        new StartManager(dayManager, true);

        // Day 2 이후 (예시)
        new StartManager(dayManager, false);
    }
}
