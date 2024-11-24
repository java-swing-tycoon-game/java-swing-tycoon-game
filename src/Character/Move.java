package Character;

import javax.swing.*;
import java.util.ArrayList;

public class Move extends JPanel {
    protected int characterX;
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

        System.out.println(targetX + " " + targetY+ " " +characterX+ " " +characterY);

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


//
//public class Move extends JPanel {
//    // 캐릭터의 현재 위치
//    protected int characterX;
//    protected int characterY;
//
//    protected int targetX;
//    protected int targetY;
//
//    protected int moveSpeed = 5;
//    //private Timer moveTimer;
//    protected ArrayList<Place> places;
//
//    public Move() {
//        places = Place.createPlaces();
//    }
//
//    // 시작 위치 설정
//    public Move(int x, int y) {
//        this(); // 기본 생성자를 호출
//        this.characterX = x;
//        this.characterY = y;
//        this.targetX = x;
//        this.targetY = y;
//    }
//
//    public ArrayList<Place> getPlaces() {
//        return places;
//    }
//
//    public void setMoveSpeed(int speed) {
//        this.moveSpeed = speed;
//    }
//
//    public int getMoveSpeed() {
//        return moveSpeed;
//    }
//
//    int centerX = 512;
//    int centerY = 330;
//
//    private void moveToCenter()
//    {
//        int dx = centerX - characterX;
//        int dy = centerY - characterY;
//        double distance = Math.sqrt(dx * dx + dy * dy);
//
//        if (distance < moveSpeed) {
//            characterX = centerX;
//            characterY = centerY;
//        } else {
//            characterX += (int) (moveSpeed * dx / distance);
//            characterY += (int) (moveSpeed * dy / distance);
//        }
//    }
//
//    private void moveCharacter() {
//        int dx = targetX - characterX;
//        int dy = targetY - characterY;
//        double distance = Math.sqrt(dx * dx + dy * dy);
//
//        if (distance < moveSpeed) {
//            characterX = targetX;
//            characterY = targetY;
//        } else {
//            characterX += (int) (moveSpeed * dx / distance);
//            characterY += (int) (moveSpeed * dy / distance);
//        }
//    }
//
////    protected void moveToDest(Place place) {
////        // 목표 좌표 설정
////        targetX = place.targetX;
////        targetY = place.targetY;
////
////        // 먼저 중앙으로 이동
////        Timer centerTimer = new Timer(15, e -> {
////            // 중앙으로 이동하는 동안
////            if (characterX != centerX || characterY != centerY) {
////                moveToCenter(); // 중앙으로 이동
////                repaint();
////            }
////            else {
////                // 중앙으로 이동이 끝난 후 목표 좌표로 이동 시작
////                ((Timer) e.getSource()).stop(); // 중앙 이동 타이머 중지
////
////                Timer moveTimer = new Timer(15, moveEvent -> {
////                    moveCharacter(); // 목표 좌표로 이동
////                    repaint();
////
////                    // 목표 좌표에 도달하면 타이머 종료
////                    if (characterX == targetX && characterY == targetY) {
////                        ((Timer) moveEvent.getSource()).stop();
////                        if(place == places.get(7)) {
////                            new Deco();
////                        }
////                    }
////                });
////                moveTimer.start();
////            }
////        });
////        centerTimer.start();
////    }
//
//    public void moveToDest(Place place) {
//        // 목표 좌표 설정
//        targetX = place.targetX;
//        targetY = place.targetY;
//
//        // 먼저 목표 좌표로 이동
//        Timer moveTimer = new Timer(15, moveEvent -> {
//            moveCharacter(); // 목표 좌표로 이동
//            repaint();
//
//            // 목표 좌표에 도달하면 타이머 종료
//            if (characterX == targetX && characterY == targetY) {
//                ((Timer) moveEvent.getSource()).stop();
//
//                if (place == places.get(7)) {
//                    new Deco();
//                }
//
//                // 목표 좌표에 도달한 후 중앙으로 이동 시작
//                Timer centerTimer = new Timer(15, centerEvent -> {
//                    if (characterX != centerX || characterY != centerY) {
//                        moveToCenter(); // 중앙으로 이동
//                        repaint();
//                    } else {
//                        ((Timer) centerEvent.getSource()).stop(); // 중앙 이동 타이머 중지
//                    }
//                });
//                centerTimer.start();
//            }
//        });
//        moveTimer.start();
//    }
//
//    public void NPCmoveToDest(Place place) {
//        // 목표 좌표 설정
//        targetX = place.targetX;
//        targetY = place.targetY;
//
//        // 먼저 중앙으로 이동
//        Timer centerTimer = new Timer(15, e -> {
//            // 중앙으로 이동하는 동안
//            if (characterX != centerX || characterY != centerY) {
//                moveToCenter(); // 중앙으로 이동
//                repaint();
//            }
//            else {
//                // 중앙으로 이동이 끝난 후 목표 좌표로 이동 시작
//                ((Timer) e.getSource()).stop(); // 중앙 이동 타이머 중지
//
//                Timer moveTimer = new Timer(15, moveEvent -> {
//                    moveCharacter(); // 목표 좌표로 이동
//                    repaint();
//
//                    // 목표 좌표에 도달하면 타이머 종료
//                    if (characterX == targetX && characterY == targetY) {
//                        ((Timer) moveEvent.getSource()).stop();
//                        if(place == places.get(7)) {
//                            new Deco();
//                        }
//                    }
//                });
//                moveTimer.start();
//            }
//        });
//        centerTimer.start();
//    }
//
//
//    public void moveToWait(Runnable callback)
//    {
//        // 목표 좌표 설정
//        targetX = 510;
//        targetY = 520;
//
//        Timer moveTimer = new Timer(15, moveEvent -> {
//            moveCharacter(); // 목표 좌표로 이동
//            repaint();
//
//            // 목표 좌표에 도달하면 타이머 종료
//            if (characterX == targetX && characterY == targetY) {
//                ((Timer) moveEvent.getSource()).stop();
//
//                // 콜백 실행
//                if (callback != null) {
//                    callback.run();
//                }
//            }
//        });
//        moveTimer.start();
//    }
//}


