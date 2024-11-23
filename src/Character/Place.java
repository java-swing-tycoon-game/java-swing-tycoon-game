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

    public int getNum(){
        return num;
    }
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

        places.add(new Place(1, (int) 72.5, (int) 437.5, (int) 35, (int) 90, 520));  // 아이템1
        places.add(new Place(1, (int) 132.5, (int) 417.5, (int) 35, (int) 140, 500));  // 아이템2
        places.add(new Place(1, (int) 192.5, (int) 397.5, (int) 35, (int) 200, 480));  // 아이템3

        places.add(new Place(1, (int) 837.5, (int) 392.5, (int) 35, 820, 480));  // 아이템4
        places.add(new Place(1, (int) 897.5, (int) 412.5, (int) 35, 880, 500));  // 아이템5
        places.add(new Place(1, (int) 960.5, (int) 434.5, (int) 35, 930, 520));  // 아이템6

        places.add(new Place(2, 285, 245, 75, 300, 220));  // 굿즈
        places.add(new Place(2, 525, 175, 75, 525, 175));  // 꾸미기
        places.add(new Place(2, 775, 245, 75, 790, 250));  // 무비

        places.add(new Place(3,510, 520, 10, 900, 520));  // 대기존1
        places.add(new Place(3,  540, 520, 10, 900, 520));  // 대기존1

        places.add(new Place(4, 745, 400, 35, 670, 420)); // 쓰레기통

        return places;
    }
}


