package Character;

import java.util.ArrayList;

public class Place {
    int num;
    int x, y, radius;
    int targetX, targetY;

    // Circle 클래스의 생성자
    public Place(int num, int x, int y, int radius, int targetX, int targetY) {
        this.num = num;
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

    public int getNum(){ return num; }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getRadius() {
        return radius;
    }
    public int getTargetX() {
        return targetX;
    }
    public int getTargetY() {
        return targetY;
    }

    // 원 객체들을 관리하는 메서드 (원 객체들의 리스트를 반환)
    public static ArrayList<Place> createPlaces() {
        ArrayList<Place> places = new ArrayList<>();

        // num1: 아이템 위치, num2: 장소, num3: 대기존, num4: 쓰레기통

        // 아이템1~3
        places.add(new Place(1, 72, 437, 35, 90, 520));
        places.add(new Place(1, 132, 417, 35, 140, 500));
        places.add(new Place(1, 192, 397, 35, 200, 480));

        // 아이템4~6
        places.add(new Place(1, 837, 392, 35, 820, 480));
        places.add(new Place(1, 897, 412, 35, 880, 500));
        places.add(new Place(1, 960, 434, 35, 930, 520));

        // 굿즈
        places.add(new Place(2, 285, 245, 75, 300, 220));
        // 꾸미기
        places.add(new Place(2, 525, 175, 75, 525, 175));
        // 무비
        places.add(new Place(2, 775, 245, 75, 790, 250));

        // 대기 구역
        places.add(new Place(3,510, 520, 10, 900, 520));
        places.add(new Place(3,  540, 520, 10, 900, 520));

        // 쓰레기통
        places.add(new Place(4, 745, 400, 35, 670, 420));

        return places;
    }
}


