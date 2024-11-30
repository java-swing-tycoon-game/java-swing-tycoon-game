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
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        textLabel.setFont(FontManager.loadFont(150f));
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
        Timer secondStageTimer = new Timer(2500, e -> {
            ((Timer) e.getSource()).stop();

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

            contentPanel.removeAll();

            int day = dayManager.getDay();
            int coinValue = (day > 0 && day <= coins.length) ? coins[day - 1] : 0;

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

                Timer scaleUpTimer = new Timer(10, scaleEvent -> {
                    float currentFontSize = rotatingTextLabel.getFont().getSize2D();
                    if (currentFontSize < 200f) {
                        rotatingTextLabel.setFont(FontManager.loadFont(currentFontSize + 2));
                        rotatingTextLabel.repaint();
                    } else {
                        ((Timer) scaleEvent.getSource()).stop();

                        Timer playTimer = new Timer(500, playEvent -> {
                            dispose();
                            new Play();
                        });
                        playTimer.setRepeats(false);
                        playTimer.start();
                    }
                });
                scaleUpTimer.start();
            });
            stopRotationTimer.setRepeats(false);
            stopRotationTimer.start();
        });
        secondStageTimer.setRepeats(false);
        secondStageTimer.start();
    }


    class RotatingTextLabel extends JLabel {
        private double angle = 0; // 회전 각도

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
