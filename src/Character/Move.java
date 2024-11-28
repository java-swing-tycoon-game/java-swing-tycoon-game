package Character;

import javax.swing.*;
import java.util.ArrayList;

public class Move extends JPanel {
    // 캐릭터의 좌표
    public int characterX;
    public int characterY;

    // 이동 목표 좌표
    protected int targetX;
    protected int targetY;

    // 장소 저장할 리스트 - 개별 장소는 Place에
    public static ArrayList<Place> places;

    // 이동 속도
    protected int moveSpeed = 5;
    // 이동에 필요한 타이머
    protected Timer moveTimer;

    public Move() {
        places = Place.createPlaces();
    }

    // 원하는 위치에 캐릭터 생성하도록
    public Move(int x, int y) {
        this(); // 기본 생성자, 장소 배열 만든다
        this.characterX = x;
        this.characterY = y;
        this.targetX = x;
        this.targetY = y;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }
    public void setMoveSpeed(int speed) {
        this.moveSpeed = speed;
    }
    public int getMoveSpeed() {
        return moveSpeed;
    }

    // 캐릭터 이동 시키는 함수
    protected void moveCharacter() {
        int dx = targetX - characterX;
        int dy = targetY - characterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < moveSpeed) {
            characterX = targetX;
            characterY = targetY;
        } else {
            characterX += (int) (moveSpeed * dx / distance);
            characterY += (int) (moveSpeed * dy / distance);
        }
    }

    // 출발 > (중앙) > 목적지 이동
    public void moveToDest(Place place, boolean viaCenter, Runnable callback) {
        if (moveTimer != null) {
            moveTimer.stop();
        }

        if (viaCenter) {
            moveToCenter(() -> moveToTarget(place, callback));
        } else {
            moveToTarget(place, callback);
        }
    }

    // 중앙으로 이동
    public void moveToCenter(Runnable callback) {
        // places를 이용해 중앙의 좌표를 설정
        int centerX = places.getFirst().getTargetX();
        int centerY = places.getFirst().getTargetY();

        moveTimer = new Timer(15, e -> {
            int dx = centerX - characterX;
            int dy = centerY - characterY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < moveSpeed) {
                characterX = centerX;
                characterY = centerY;
                ((Timer) e.getSource()).stop();

                if (callback != null) {
                    callback.run();
                }
            } else {
                characterX += (int) (moveSpeed * dx / distance);
                characterY += (int) (moveSpeed * dy / distance);
            }
            repaint();
        });
        moveTimer.start();
    }

    // 목적지로 이동
    public void moveToTarget(Place place, Runnable callback) {
        targetX = place.targetX;
        targetY = place.targetY;

        moveTimer = new Timer(15, e -> {
            moveCharacter();
            repaint();

            if (characterX == targetX && characterY == targetY) {
                ((Timer) e.getSource()).stop();

                if (callback != null) {
                    callback.run();
                }
            }
        });
        moveTimer.start();
    }
}
