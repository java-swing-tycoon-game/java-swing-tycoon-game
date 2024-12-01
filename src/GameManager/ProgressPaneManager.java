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
    // private final int[] dayTimes = {60, 50, 45, 40, 35, 30, 25}; // 각 day의 초기 시간 final int 배열로 고정..
    private final int[] dayTimes = {6, 6, 6, 6, 6, 6, 6};
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

    public ProgressPaneManager() {
        this.dayPanel = new ImageDayPanel(); // dayPanel 초기화
        this.progressPane = new ImageProgressPane(); // progressPane 초기화
    }
  
    public JPanel getProgressPane() {
        return progressPane;
    }

    public void startDayTimer() {
        if (dayTimer != null) {
            dayTimer.cancel();
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
                    DayManager.getInstance().nextDay(); // DayManager의 상태를 증가

                    if (dayManager.getDay() == 1) {new StartManager(DayManager.getInstance(), true);}
                    else {new StartManager(DayManager.getInstance(), false);} // 증가된 상태로 StartManager 시작

                    startDayTimer(); // 새로운 Day와 관련된 타이머 시작
                    // new StartManager(DayManager.getInstance(), dayManager.getDay() == 1);
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
            EndingManager endingManager = new EndingManager(dayManager, coinManager);  // EndingManager 생성
            endingManager.setVisible(true);  // 게임 오버 화면 띄우기
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

    // 데이 이미지 관련
    public class ImageDayPanel extends JPanel {
        private JLabel dayLabel;

        public ImageDayPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setOpaque(false);

            dayLabel = new JLabel();
            updateDayImage(dayManager.getDay());    // 데이 이미지 초기화
            add(dayLabel);
        }

        // Day 변경 시 데이 이미지와 장소 가시성을 함께 변경
        public void updateDayImage(int day) {
            try {
                // Day 이미지 업데이트
                String dayImagePath = "assets/img/day" + day + ".png";
                BufferedImage dayImage = ImageIO.read(new File(dayImagePath));
                dayLabel.setIcon(new ImageIcon(dayImage));
            } catch (IOException ex) {
                System.err.println("이미지 로드 실패: " + ex.getMessage());
                dayLabel.setIcon(null); // 이미지 로드 실패 시 초기화
            }
        }

    }
}
