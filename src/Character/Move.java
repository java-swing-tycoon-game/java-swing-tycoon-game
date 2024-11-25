package Character;

import javax.swing.*;
import java.util.ArrayList;

public class Move extends JPanel {
    public int characterX;
    protected int characterY;

    protected int targetX;
    protected int targetY;

    protected int moveSpeed = 5;

    protected ArrayList<Place> places;

    private Timer currentTimer; // 현재 실행 중인 타이머

    public Move() {
        places = Place.createPlaces();
    }

    public Move(int x, int y) {
        this(); // 기본 생성자 호출
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

    public void moveToDest(Place place, boolean viaCenter, Runnable callback) {
        if (currentTimer != null) {
            currentTimer.stop(); // 기존 타이머 정지
        }

        if (viaCenter) {
            moveToCenter(() -> moveToTarget(place, callback));
        } else {
            moveToTarget(place, callback);
        }
    }

    public void moveToCenter(Runnable callback) {
        int centerX = 512;
        int centerY = 330;

        currentTimer = new Timer(15, e -> {
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
        currentTimer.start();
    }

    public void moveToTarget(Place place, Runnable callback) {
        targetX = place.targetX;
        targetY = place.targetY;

        System.out.println("이동 목표: " + targetX + ", " + targetY+ "현재 위치: " +characterX+ " " +characterY);

        currentTimer = new Timer(15, e -> {
            moveCharacter();
            repaint();

            if (characterX == targetX && characterY == targetY) {
                ((Timer) e.getSource()).stop();

                if (callback != null) {
                    callback.run();
                }
            }
        });
        currentTimer.start();
    }
}
