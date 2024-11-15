package Character;

import javax.swing.*;
import java.util.ArrayList;

public class Move extends JPanel {
    protected int characterX = 512;
    protected int characterY = 330;

    protected int targetX = 512;
    protected int targetY = 330;

    protected int moveSpeed = 5;
    private Timer moveTimer;
    protected ArrayList<Place> places;

    public Move() {
        places = Place.createPlaces();
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

    public void moveToDest(Place place) {
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
                    }
                });
                moveTimer.start();
            }
        });
        centerTimer.start();
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
}
