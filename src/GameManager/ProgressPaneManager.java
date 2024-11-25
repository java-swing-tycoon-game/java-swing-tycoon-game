package GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import Scenes.Buy;
import Scenes.Play;

public class ProgressPaneManager {
    private int realTime; // 현재 남은 시간
    private int day = 1;  // 현재 데이 (1부터 시작)
    private final int[] dayTimes = {600, 600, 60, 60, 60, 60, 60}; // 각 day의 초기 시간 (디버깅용)
    private Timer dayTimer; // 날짜 타이머
    private DayManager dayManager = new DayManager();
    private CoinManager coinManager = new CoinManager();

    private ImageProgressPane progressPane;

    public ProgressPaneManager() {
        this.progressPane = new ImageProgressPane(); // progressPane 초기화
    }

    public JPanel getProgressPane() {
        return progressPane; // 시간바 관련
    }

    // day 값을 외부에서 사용할 수 있도록 get..
    public int getDay() {
        return this.day;
    }

    // 엔딩 화면을 띄우는 함수
    private void showEndingScreen() {
        SwingUtilities.invokeLater(() -> {
            // Play 화면을 닫고 게임 오버 화면을 띄우기
            // Play.dispose();  // Play 화면 닫기
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

            startDayTimer();
        }

        private void startDayTimer() {
            realTime = dayTimes[day - 1];   // 현재 데이의 초기 시간
            progressBar.setValue(100);      // 진행률 이미지 초기화

            dayTimer = new Timer(1000, e -> {
                realTime--;
                updateTimeBar();

                // 시간이 0이 되면 Day 종료
                if (realTime <= 0) {
                    dayTimer.stop();  // 타이머 종료
                    System.out.println("Day " + day + " 종료!");

                    // 코인 기준 금액 확인
                    int[] coins = {5, 10, 20, 50, 75, 90, 100}; // 기준 금액 배열
                    if (coinManager.getCoinAmount() >= coins[day - 1]) {
                        // 기준 충족: Buy 팝업 표시
                        if (day == dayTimes.length) {
                            showEndingScreen(); // 최종 Day에서 엔딩 처리
                        } else {
                            showBuyScreen(); // Buy 창 표시
                        }
                    } else {
                        // 기준 미달: Game Over 처리
                        showEndingScreen();
                    }
                }
            });

            dayTimer.start();
        }

        // Buy 팝업 호출
        private void showBuyScreen() {
            SwingUtilities.invokeLater(() -> {
                Buy buyPopup = new Buy();
                buyPopup.setVisible(true);
            });

            // 다음 Day로 이동
            day++;
            startDayTimer();
        }


        private void updateTimeBar() {
            int initialTime = dayTimes[day - 1]; // 현재 Day의 초기 시간
            int progress = (int) ((realTime / (double) initialTime) * 100);
            progressBar.setValue(progress);
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
}
