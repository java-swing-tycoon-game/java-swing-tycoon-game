package GameManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ProgressPaneManager {
    private int realTime; // 현재 남은 시간
    private int day = 1;  // 현재 데이 (1부터 시작)
    private final int[] dayTimes = {12, 10, 8, 6, 4, 2, 1}; // 각 day의 초기 시간 (디버깅용)
    private Timer dayTimer; // 날짜 타이머

    private ImageProgressPane progressPane;

    public ProgressPaneManager() {
        this.progressPane = new ImageProgressPane(); // progressPane 초기화
    }

    public JPanel getProgressPane() {
        return progressPane; // 시간바 관련
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
            progressBar.setValue(100);  // 진행률 이미지 다시 100으로 초기화하기

            dayTimer = new Timer(1000, e -> {
                realTime--;
                updateTimeBar();

                // 남은 시간이 0이 되면 데이 종료
                if (realTime <= 0) {
                    dayTimer.stop();  // 타이머 종료
                    System.out.println("Day " + day + " 종료! 다음 Day로 이동");

                    // Day 종료 메시지 팝업
                    JOptionPane.showMessageDialog(null,
                            "Day " + day + " 종료! 다음 Day로 이동합니다.",
                            "Day 진행",
                            JOptionPane.INFORMATION_MESSAGE);

                    // 모든 Day가 끝났으면 게임 종료 메시지
                    if (day >= dayTimes.length) {
                        JOptionPane.showMessageDialog(null,
                                "모든 Day를 완료했습니다! 게임 끝!!!",
                                "게임 종료",
                                JOptionPane.INFORMATION_MESSAGE);

                        // 게임 종료 처리
                        return;
                    }

                    // 다음 Day로 이동
                    day++;
                    startDayTimer();  // 새로운 Day 타이머 시작
                }
            });

            dayTimer.start();
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
