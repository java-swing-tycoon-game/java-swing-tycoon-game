package GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DayManager {
    private static DayManager instance; // 싱글톤 인스턴스
    private int day = 1; // 현재 날짜 (1부터 시작)
    private ImageDayPanel dayPanel;

    public DayManager() {
        this.dayPanel = new ImageDayPanel(); // dayPanel 초기화
    }

    // 싱글톤 인스턴스를 가져오는 메서드
    public static synchronized DayManager getInstance() {
        if (instance == null) {
            instance = new DayManager();
        }
        return instance;
    }

    public JPanel getDayPanel() {
        return dayPanel; // 데이 이미지 관련
    }

    public int getDay() {
        return day;
    }

    // 다음 Day로 이동하고 이미지 업데이트
    public void nextDay() {
        day++; // Day 증가
        dayPanel.updateDayImage(day); // 새로운 Day 이미지로 업데이트
    }

    // 데이 이미지 관련 클래스
    public class ImageDayPanel extends JPanel {
        private JLabel dayLabel;

        public ImageDayPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            dayLabel = new JLabel();
            setOpaque(false);
            updateDayImage(day);    // 데이 이미지 초기화
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