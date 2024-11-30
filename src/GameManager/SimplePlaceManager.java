package GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimplePlaceManager extends JPanel {
    private ArrayList<Image> placeImages;
    private boolean[] visiblePlaces;
    private ArrayList<Point> placePositions;
    private DayManager dayManager;

    public SimplePlaceManager() {

        placeImages = new ArrayList<>();
        placeImages.add(new ImageIcon("assets/img/room1.png").getImage()); // 장소 1
        placeImages.add(new ImageIcon("assets/img/room2.png").getImage()); // 장소 2

        // 각 장소의 초기 가시성 설정
        visiblePlaces = new boolean[placeImages.size()];
        for (int i = 0; i < visiblePlaces.length; i++) {
            visiblePlaces[i] = false; // 모든 장소 초기에는 보이지 않도록 설정
        }

        // 각 장소의 좌표 설정
        placePositions = new ArrayList<>();
        placePositions.add(new Point(0, 0)); // 장소 1의 좌표
        placePositions.add(new Point(0, 0)); // 장소 2의 좌표

        setPreferredSize(new Dimension(1024, 768));

    }

    // 특정 장소의 가시성을 설정하는 메서드
    public void setPlaceVisible(int index, boolean visible) {
        visiblePlaces[index] = visible;
        repaint();
    }

//    // 특정 장소의 가시성을 반환
//    public boolean isPlaceVisible(int index) {
//            return visiblePlaces[index];
//    }
//
//    // day별 장소 업데이트
//    public void updatePlaceByDay() {
//        int currentDay = dayManager.getDay();
//        for (int i = 0; i < visiblePlaces.length; i++) {
//            visiblePlaces[i] = (i < currentDay);
//        }
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 장소 이미지 그리기
        for (int i = 0; i < placeImages.size(); i++) {
            if (visiblePlaces[i]) { // 보이기가 true인 경우에만 그림
                Point position = placePositions.get(i);
                g.drawImage(placeImages.get(i), position.x, position.y, null);
            }
        }
    }
}
