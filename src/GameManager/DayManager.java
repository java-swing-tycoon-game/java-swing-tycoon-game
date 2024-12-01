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
    private static int day = 1; // 현재 날짜 (1부터 시작)
    private ImageDayPanel dayPanel;

    private boolean[] itemPurchased; // 아이템 구매 상태 배열
    private boolean hasPurchasedToday[]; // 하루에 하나만 구매 가능 플래그

    public DayManager() {

        this.dayPanel = new ImageDayPanel(); // dayPanel 초기화

        this.placeManager = new SimplePlaceManager();

        this.itemPurchased = new boolean[6]; // 아이템 개수에 맞게 크기 설정 (예: 10개)
        this.hasPurchasedToday = new boolean[6];

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

    public static int getDay() {
        return day;
    }
    public void setDay() {day = 1;}

    // 다음 Day로 이동하고 이미지 업데이트
    public void nextDay() {
        day++; // Day 증가
        System.out.println("새로운 날 시작: Day " + day);

        // Day 이미지 업데이트
        dayPanel.updateDayImage(day);

        // 데이에 따른 장소 설정
        if (day == 2 || day == 3) {
            placeManager.setPlaceVisible(day - 2, true);
        }

        // 아이템 구매 초기화
        for (int i = 0; i < 6; i++) {
            hasPurchasedToday[i] = false;
        }

        // NPC 초기화 및 스폰 시작
        NpcManager.onDayChange(day);
    }

    public boolean hasPurchasedToday(int index) {
        return hasPurchasedToday[index];
    }

    public void setHasPurchasedToday(int index, boolean purchasedToday) {
        hasPurchasedToday[index] = purchasedToday;
    }

    public boolean isItemPurchased(int index) {
        return itemPurchased[index];
    }
    public void setItemPurchased(int index, boolean purchased) {
        itemPurchased[index] = purchased; // 특정 아이템 구매 상태 업데이트
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