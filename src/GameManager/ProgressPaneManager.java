package GameManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ProgressPaneManager {

    private int day = 1; // 현재 날짜 (1부터 시작)
    private int realTime; // 현재 남은 시간
    private final int[] dayTimes = {60, 50, 45, 40, 35, 30, 25}; // 각 day의 초기 시간 final int 배열로 고정..
    // private final int[] dayTimes = {12, 10, 8, 6, 4, 2, 1}; // 각 day의 초기 시간 final int 배열로 고정.. (디버깅용)
    private Timer dayTimer; // 날짜 타이머

    public JPanel getProgressPane() {
        return new ImageProgressPane();
    }

    public class ImageProgressPane extends JPanel {
        private JProgressBar progressBar; // 진행률을 표시할 JProgressBar
        private BufferedImage progressImage; // 진행률 이미지
        private BufferedImage backgroundImage; // 배경 이미지

        public ImageProgressPane() {
            setPreferredSize(new java.awt.Dimension(600, 60));  // 전체 영역 임의 설정해두기
            setLayout(null);
            setOpaque(false);

            try {
                // 이미지 로드
                progressImage = ImageIO.read(new File("assets/img/timeBar.png"));
                backgroundImage = ImageIO.read(new File("assets/img/basicTimeBar.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // JProgressBar 설정
            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(100);

            // 이미지들이 패널에 추가
            add(progressBar);

            startDayTimer();
        }

        private void startDayTimer() {
            realTime = dayTimes[day - 1];   // 현재 데이의 초기 시간
            progressBar.setValue(100);  // 진행률 이미지 다시 100으로 초기화하기

            dayTimer = new Timer(1000, e -> {
                realTime--;
                updateTimeBar();

                // 콘솔에 현재 day와 남은 시간 콘솔 출력 확인용 입니다!!!
                System.out.println("Day: " + day + ", 남은 시간: " + realTime + "초");

                if (realTime <= 0) {
                    dayTimer.stop();
                    System.out.println("Day " + day + " 종료! 다음 Day로 이동");   // 데이 종료된 거 콘솔 출력 확인용 입니다!!!
                    resetDay(); // 타이머 종료 후 다음 day로 초기화
                }
            });

            dayTimer.start();
        }

        private void resetDay() {
            day++; // 다음 day로 이동

            if (day > dayTimes.length) {
                // 모든 Day 완료 후 게임 종료 팝업창 표시
                JOptionPane.showMessageDialog(null,
                        "모든 Day를 완료했습니다! 게임 끝!!!",
                        "게임 종료",
                        JOptionPane.INFORMATION_MESSAGE);

                // 이후 타이머 중지 및 게임 종료 처리
                if (dayTimer != null) {
                    dayTimer.stop();
                }
                return;
            }

            // 다음 Day로 이동하기 전에 팝업창 표시
            JOptionPane.showMessageDialog(null,
                    "Day " + (day - 1) + " 종료! 다음 Day로 이동합니다.",
                    "Day 진행",
                    JOptionPane.INFORMATION_MESSAGE);

            startDayTimer(); // 새로운 Day 타이머 시작
        }

        private void updateTimeBar() {
            // 진행률 업데이트
            int initialTime = dayTimes[day - 1];
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
