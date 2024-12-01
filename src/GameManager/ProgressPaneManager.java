package GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import Scenes.Buy;
import Scenes.Play;

public class ProgressPaneManager {
    private final int[] dayTimes = {60, 50, 45, 40, 35, 30, 25}; // 각 Day의 초기 시간
    private int realTime; // 현재 남은 시간
    private Timer dayTimer; // 날짜 타이머

    private DayManager dayManager;
    private boolean isBuyPopupOpen = false;
    private CoinManager coinManager = new CoinManager();
    private ImageProgressPane progressPane;
    private boolean isPaused = false;

    private Play playInstance;

    public ProgressPaneManager(DayManager dayManager) {
        this.dayManager = dayManager.getInstance();
        this.progressPane = new ImageProgressPane();
    }

    public JPanel getProgressPane() {
        return progressPane;
    }

    public void startDayTimer() {
        if (dayTimer != null) {
            dayTimer.cancel();
        }

        if (dayManager.getDay() != 1) {
            pauseTimerForSeconds(2);
        }

        realTime = dayTimes[dayManager.getDay() - 1];
        progressPane.updateProgress(100);

        dayTimer = new Timer();
        dayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isPaused) {
                    return; // 타이머가 일시정지된 상태에서는 동작하지 않음
                }

                realTime--;
                int progress = (int) ((realTime / (double) dayTimes[dayManager.getDay() - 1]) * 100);
                progressPane.updateProgress(progress);

                if (realTime <= 0) {
                    dayTimer.cancel();
                    handleTimerEnd();
                }
            }
        }, 1000, 1000);
    }

    private void handleTimerEnd() {
        if (coinManager.getCoinAmount() >= coinManager.coins[dayManager.getDay() - 1]) {
            if (dayManager.getDay() == dayTimes.length) {
                showEndingScreen();
            } else {
                showBuyScreen();
            }
        } else {
            showEndingScreen();
        }
    }

    private void showBuyScreen() {
        if (isBuyPopupOpen) {
            return;
        }
        isBuyPopupOpen = true;

        SwingUtilities.invokeLater(() -> {
            Buy buyPopup = new Buy();
            buyPopup.setOnDisposeAction(() -> {
                isBuyPopupOpen = false;

                if (buyPopup.isNextButtonClicked()) {
                    if (dayManager.getDay() < 7) {
                        dayManager.nextDay();
                        new StartManager(dayManager, dayManager.getDay() == 1);
                        startDayTimer();
                    }
                }
            });

            buyPopup.setVisible(true);
        });
    }

    private void showEndingScreen() {
        SwingUtilities.invokeLater(() -> {
            if (playInstance != null) {
                playInstance.dispose();
            }
            JFrame parentFrame = playInstance != null ? playInstance : new JFrame();
            EndingManager endingManager = new EndingManager(parentFrame, dayManager, coinManager); // EndingManager 생성
            endingManager.setVisible(true); // 게임 오버 화면 띄우기
        });
    }

    public void pauseTimerForSeconds(int seconds) {
        isPaused = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isPaused = false;
            }
        }, seconds * 1000);
    }

    public class ImageProgressPane extends JPanel {
        private JProgressBar progressBar;
        private BufferedImage progressImage;
        private BufferedImage backgroundImage;

        public ImageProgressPane() {
            setPreferredSize(new Dimension(600, 60));
            setLayout(null);
            setOpaque(false);

            try {
                progressImage = ImageIO.read(new File("assets/img/timeBar.png"));
                backgroundImage = ImageIO.read(new File("assets/img/basicTimeBar.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(100);

            add(progressBar);
        }

        public void updateProgress(int value) {
            progressBar.setValue(value);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight() - 50, this);
            }

            if (progressImage != null) {
                int width = (int) (getWidth() * progressBar.getPercentComplete());
                g2d.drawImage(progressImage, 5, 6, width - 2, getHeight() - 64, this);
            }

            g2d.dispose();
        }
    }
}
