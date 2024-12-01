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
    private final int[] dayTimes = {30, 50, 45, 40, 35, 30, 25}; // 각 day의 초기 시간 final int 배열로 고정..
    private int realTime; // 현재 남은 시간
    private Timer dayTimer; // 날짜 타이머
    private DayManager dayManager;
    private boolean isBuyPopupOpen = false; // Buy 팝업 중복 방지 플래그
    private CoinManager coinManager = new CoinManager();
    private ImageProgressPane progressPane;

    private ImageDayPanel dayPanel;
    private Play playInstance; // Play 인스턴스 추가

    public ProgressPaneManager(DayManager dayManager) {
        this.dayManager = dayManager;
        this.progressPane = new ImageProgressPane();
    }

//    public ProgressPaneManager() {
//        this.dayPanel = new ImageDayPanel(); // dayPanel 초기화
//        this.progressPane = new ImageProgressPane(); // progressPane 초기화
//    }

    public JPanel getProgressPane() {
        return progressPane; // 시간바 관련
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

                    if (coinManager.getCoinAmount() >= coinManager.coins[dayManager.getDay() - 1]) {
                        if (dayManager.getDay() == dayTimes.length) {
                            showEndingScreen();
                        }
                        else {
                            showBuyScreen();
                        }
                    }
                    else {
                        showEndingScreen();
                    }
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
                    DayManager.getInstance().nextDay(); // DayManager의 상태를 증가
                    if (dayManager.getDay() == 1) {new StartManager(DayManager.getInstance(), true);}
                    else {new StartManager(DayManager.getInstance(), false);} // 증가된 상태로 StartManager 시작
                    startDayTimer(); // 새로운 Day와 관련된 타이머 시작
                }
            });

            buyPopup.setVisible(true);
        });


    }

    public JPanel getDayPanel() {
        return dayPanel; // 데이 이미지 관련
    }

    // 엔딩 화면을 띄우는 함수
    private void showEndingScreen() {
        SwingUtilities.invokeLater(() -> {
            if (playInstance != null) {
                playInstance.dispose();  // Play 화면 닫기
            }

            EndingManager endingManager = new EndingManager(dayManager, coinManager);  // EndingManager 생성
            endingManager.setVisible(true);  // 게임 오버 화면 띄우기
        });
    }

    // 시간바 관련 클래스
    public class ImageProgressPane extends JPanel {
        private JProgressBar progressBar;   // 진행률을 표시할 JProgressBar
        private BufferedImage progressImage;    // 진행률 이미지
        private BufferedImage backgroundImage;  // 배경 이미지

        public ImageProgressPane() {
            setPreferredSize(new java.awt.Dimension(600, 60));  // 전체 영역 임의로 설정해두기
            setLayout(null);
            setOpaque(false);   // 시간바 배경 투명도

            try {
                progressImage = ImageIO.read(new File("assets/img/timeBar.png"));
                backgroundImage = ImageIO.read(new File("assets/img/basicTimeBar.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // JProgressBar 설정
            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(100);

            // 이미지들 패널에 추가
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

            // 배경 이미지 그리기
            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight() - 50, this);
            }

            // 진행률 이미지 그리기
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

        // day 변경 시 데이 이미지도 변경
        public void updateDayImage(int day) {
            try {
                String imagePath = "assets/img/day" + day + ".png";
                BufferedImage dayImage = ImageIO.read(new File(imagePath));
                dayLabel.setIcon(new ImageIcon(dayImage));
            } catch (IOException ex) {
                System.err.println("이미지 로드 실패: " + ex.getMessage());
                dayLabel.setIcon(null); // 이미지 로드 실패 시 아이콘 초기화
            }
        }
    }
}