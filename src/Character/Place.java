package Character;

import java.util.ArrayList;

public class Place {
    int x, y, radius;
    int targetX, targetY;

    // Circle 클래스의 생성자
    public Place(int x, int y, int radius, int targetX, int targetY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    // 클릭한 좌표가 원 안에 포함되어 있는지 확인하는 메서드
    public boolean contains(int mouseX, int mouseY) {
        int dx = mouseX - x;
        int dy = mouseY - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    // 원 객체들을 관리하는 메서드 (원 객체들의 리스트를 반환)
    public static ArrayList<Place> createPlaces() {
        ArrayList<Place> places = new ArrayList<>();
        places.add(new Place((int) 72.5, (int) 437.5, (int) 22.5, (int) 90, 520));  // 아이템1
        places.add(new Place((int) 132.5, (int) 417.5, (int) 22.5, (int) 140, 500));  // 아이템2
        places.add(new Place((int) 192.5, (int) 397.5, (int) 22.5, (int) 200, 480));  // 아이템3

        places.add(new Place((int) 842.5, (int) 397.5, (int) 22.5, 820, 480));  // 아이템4
        places.add(new Place((int) 902.5, (int) 417.5, (int) 22.5, 880, 500));  // 아이템5
        places.add(new Place((int) 962.5, (int) 437.5, (int) 22.5, 930, 520));  // 아이템6

        places.add(new Place(285, 245, 75, 300, 220));  // 굿즈
        places.add(new Place(525, 175, 75, 525, 175));  // 꾸미기
        places.add(new Place(775, 245, 75, 790, 250));  // 무비

        places.add(new Place(510, 520, 10, 900, 520));  // 대기존1
        places.add(new Place(540, 520, 10, 900, 520));  // 대기존1

        return places;
    }
}


