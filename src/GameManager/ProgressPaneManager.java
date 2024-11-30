package GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import Scenes.Buy;

public class ProgressPaneManager {
    private final int[] dayTimes = {2, 6, 10000, 6, 6, 6, 6}; // 각 Day의 초기 시간
    private int realTime; // 현재 남은 시간
    private Timer dayTimer; // Day 타이머
    private DayManager dayManager;
    private boolean isBuyPopupOpen = false; // Buy 팝업 중복 방지 플래그

    private ImageProgressPane progressPane;

    public ProgressPaneManager(DayManager dayManager) {
        this.dayManager = dayManager;
        this.progressPane = new ImageProgressPane();
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
                realTime--;
                int progress = (int) ((realTime / (double) dayTimes[dayManager.getDay() - 1]) * 100);
                progressPane.updateProgress(progress);

                if (realTime <= 0) {
                    dayTimer.cancel();
                    showBuyScreen();
                }
            }
        }, 1000, 1000);
    }

    private void showBuyScreen() {
        if (isBuyPopupOpen) {
            return; // 중복 생성 방지
        }
        isBuyPopupOpen = true;

        SwingUtilities.invokeLater(() -> {
            Buy buyPopup = new Buy();

            buyPopup.setOnDisposeAction(() -> {
                isBuyPopupOpen = false;
                if (buyPopup.isNextButtonClicked()) {
                    dayManager.nextDay();
                    startDayTimer();
                }
            });

            buyPopup.setVisible(true);
        });
    }

    public class ImageProgressPane extends JPanel {
        private JProgressBar progressBar;

        public ImageProgressPane() {
            setLayout(new BorderLayout());
            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(100);
            add(progressBar, BorderLayout.CENTER);
        }

        public void updateProgress(int value) {
            progressBar.setValue(value);
            repaint();
        }
    }
}
