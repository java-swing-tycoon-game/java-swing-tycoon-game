package Character;

import Scenes.Deco;

import javax.swing.*;
import java.util.ArrayList;

public class Move extends JPanel {
    // 캐릭터의 현재 위치
    protected int characterX;
    protected int characterY;

    protected int targetX;
    protected int targetY;

    protected int moveSpeed = 5;
    //private Timer moveTimer;
    protected ArrayList<Place> places;

    public Move() {
        places = Place.createPlaces();
    }

    // 시작 위치 설정
    public Move(int x, int y) {
        this(); // 기본 생성자를 호출
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

    int centerX = 512;
    int centerY = 330;

    private void moveToCenter()
    {
        int dx = centerX - characterX;
        int dy = centerY - characterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < moveSpeed) {
            characterX = centerX;
            characterY = centerY;
        } else {
            characterX += (int) (moveSpeed * dx / distance);
            characterY += (int) (moveSpeed * dy / distance);
        }
    }

    private void moveCharacter() {
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

    protected void moveToDest(Place place) {
        // 목표 좌표 설정
        targetX = place.targetX;
        targetY = place.targetY;

        // 먼저 중앙으로 이동
        Timer centerTimer = new Timer(15, e -> {
            // 중앙으로 이동하는 동안
            if (characterX != centerX || characterY != centerY) {
                moveToCenter(); // 중앙으로 이동
                repaint();
            }
            else {
                // 중앙으로 이동이 끝난 후 목표 좌표로 이동 시작
                ((Timer) e.getSource()).stop(); // 중앙 이동 타이머 중지

                Timer moveTimer = new Timer(15, moveEvent -> {
                    moveCharacter(); // 목표 좌표로 이동
                    repaint();

                    // 목표 좌표에 도달하면 타이머 종료
                    if (characterX == targetX && characterY == targetY) {
                        ((Timer) moveEvent.getSource()).stop();
                        if(place == places.get(7)) {
                            new Deco();
                        }
                    }
                });
                moveTimer.start();
            }
        });
        centerTimer.start();
    }

    protected void moveToWait(Runnable callback)
    {
        // 목표 좌표 설정
        targetX = 510;
        targetY = 520;

        Timer moveTimer = new Timer(15, moveEvent -> {
            moveCharacter(); // 목표 좌표로 이동
            repaint();

            // 목표 좌표에 도달하면 타이머 종료
            if (characterX == targetX && characterY == targetY) {
                ((Timer) moveEvent.getSource()).stop();

                // 콜백 실행
                if (callback != null) {
                    callback.run();
                }
            }
        });
        moveTimer.start();
    }
}
