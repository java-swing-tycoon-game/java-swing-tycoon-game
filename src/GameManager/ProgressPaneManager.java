package GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import Scenes.Main;
import Scenes.Buy;
import Scenes.Play;

public class ProgressPaneManager {
    private final int[] dayTimes = {50, 70, 80, 80, 80, 80, 80}; // 각 day의 초기 시간 final int 배열로 고정..
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

    private boolean isEndingScreenShown = false; // 엔딩 화면 중복 호출 방지 플래그

    private void showEndingScreen() {
        if (isEndingScreenShown) {
            return; // 이미 엔딩 화면이 표시된 경우 중복 호출 방지
        }

        isEndingScreenShown = true; // 플래그 설정

        SwingUtilities.invokeLater(() -> {
            // Play 창이 열려 있다면 닫기
            if (playInstance != null) {
                playInstance.dispose();
                playInstance = null; // 참조 해제
            }

            // 성공 여부 확인
            boolean isSuccess = CoinManager.getCoinAmount() >= 100 && DayManager.getDay() == 7;

            // 성공/실패에 따라 엔딩 화면 표시
            EndingManager endingManager = new EndingManager(null, dayManager, coinManager, isSuccess);
            endingManager.setVisible(true);

            // 엔딩 화면이 닫힌 후 메인 화면으로 이동
            goToMainScreen();
        });
    }

    private void goToMainScreen() {
        SwingUtilities.invokeLater(() -> {
            Main mainScreen = new Main();
            mainScreen.setVisible(true);
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
