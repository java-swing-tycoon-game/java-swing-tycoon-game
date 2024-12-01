package GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DayManager {
    private static DayManager instance; // 싱글톤 인스턴스
    CoinManager coinManager = new CoinManager();
    private SimplePlaceManager placeManager; // 장소 관리
    private int day = 1; // 현재 날짜 (1부터 시작)
    private ImageDayPanel dayPanel;

    public DayManager() {
        this.dayPanel = new ImageDayPanel(); // dayPanel 초기화
        this.placeManager = new SimplePlaceManager();
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

    public SimplePlaceManager getPlaceManager() {
        return placeManager; // 장소 매니저 반환
    }

    public int getDay() {
        return day;
    }

    // 다음 Day로 이동하고 이미지 업데이트
    public void nextDay() {
        day++; // Day 증가

        dayPanel.updateDayImage(day);
        // Day 변경에 따라 장소 가시성 갱신
        if (day == 2 || day == 3) {
            placeManager.setPlaceVisible(day - 2, true); // Day 2부터 장소 보이기
        }
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

        // Day 변경 시 데이 이미지 변경
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