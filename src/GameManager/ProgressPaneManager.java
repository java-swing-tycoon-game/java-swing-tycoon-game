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
    // private final int[] dayTimes = {60, 50, 45, 40, 35, 30, 25}; // 각 day의 초기 시간 final int 배열로 고정..
    private final int[] dayTimes = {6,6,6,6,6,6,6}; // 각 day의 초기 시간 final int 배열로 고정.. (디버깅용)
    private Timer dayTimer; // 날짜 타이머
    private DayManager dayManager = new DayManager();
    private CoinManager coinManager = new CoinManager();

    private ImageDayPanel dayPanel;
    private Play playInstance; // Play 인스턴스 추가
    private ImageProgressPane progressPane;

    public ProgressPaneManager(Play playInstance) {
        this.playInstance = playInstance; // Play 인스턴스 저장
        this.progressPane = new ImageProgressPane(); // progressPane 초기화
    }

    public ProgressPaneManager() {
        this.dayPanel = new ImageDayPanel(); // dayPanel 초기화
        this.progressPane = new ImageProgressPane(); // progressPane 초기화
    }

    public JPanel getProgressPane() {
        return progressPane; // 시간바 관련
    }

    public JPanel getDayPanel() {
        return dayPanel; // 데이 이미지 관련
    }

    // day 값을 DayManager에서 가져옴
    public int getDay() {
        return dayManager.getDay();
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

            startDayTimer();
        }

        private void startDayTimer() {
            realTime = dayTimes[getDay() - 1];   // 현재 데이의 초기 시간
            progressBar.setValue(100);  // 진행률 이미지 다시 100으로 초기화하기

            dayTimer = new Timer(1000, e -> {
                realTime--;
                updateTimeBar();

                if (realTime <= 0) {
                    dayTimer.stop();
                    System.out.println("Day " + getDay() + " 종료! 다음 Day로 이동");   // 데이 종료된 거 콘솔 출력 확인용 입니다!!!
                    resetDay(); // 타이머 종료 후 다음 day로 초기화
                }
            });

            dayTimer.start();
        }

        private void showBuyScreen() {
            SwingUtilities.invokeLater(() -> {
                Buy buyPopup = new Buy();
                buyPopup.setVisible(true);

                // Buy 창이 닫힌 후 버튼 클릭 상태 확인
                if (buyPopup.isNextButtonClicked()) {
                    //startDayTimer(); // 새로운 Day 타이머 시작
                    dayManager.nextDay(); // 다음 Day로 이동
                    new StartManager();
                }
            });

            // dayManager.nextDay(); // 다음 Day로 이동
        }

        private void resetDay() {
            if (coinManager.getCoinAmount() >= coinManager.coins[getDay() - 1]) {
                if (getDay() == dayTimes.length) {
                    showEndingScreen(); // 마지막 Day 엔딩 처리
                    if (dayTimer != null) {
                        dayTimer.stop();
                    }
                    return;
                } else {
                    dayTimer.stop();
                    showBuyScreen(); // Buy 창 표시
                    // dayManager.nextDay();  // 다음 day로 이동
                }
            } else {
                showEndingScreen(); // 기준 미달 시 Game Over
                return;
            }

            // startDayTimer(); // 새로운 Day 타이머 시작

            // 데이 이미지 변경
            dayPanel.updateDayImage(getDay());
        }

        private void updateTimeBar() {
            // 진행률 업데이트
            int initialTime = dayTimes[getDay() - 1];
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

    // 데이 이미지 관련
    public class ImageDayPanel extends JPanel {
        private JLabel dayLabel;

        public ImageDayPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setOpaque(false);

            dayLabel = new JLabel();
            updateDayImage(getDay());    // 데이 이미지 초기화
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