package Player;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Move extends JPanel {
    // 캐릭터의 현재 좌표
    private int characterX = 512;
    private int characterY = 330;

    // 캐릭터가 이동할 목표 좌표
    private int targetX = 512;
    private int targetY = 330;

    // 이동 속도
    private int moveSpeed = 5;

//    private Image characterImg;

//    public Move() {
//        characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
//        // 패널을 투명하게 설정
//        setOpaque(false);
//
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                // 클릭 위치를 목표 위치로 설정
//                targetX = e.getX();
//                targetY = e.getY();
//            }
//        });
//
//        Timer timer = new Timer(15, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                moveCharacter();
//                repaint(); // 화면 갱신
//            }
//        });
//        timer.start();
//    }


    private ArrayList<Place> places;  // 원들을 관리하는 리스트

    private Image characterImg;

    public Move() {
        characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();

        // Circle 클래스의 createCircles 메서드를 통해 원 객체들을 리스트에 추가
        places = Place.createPlaces();

        // 패널을 투명하게 설정
        setOpaque(false);

        // 마우스 클릭 리스너 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // 각 원을 검사하여 클릭된 경우 해당 원의 목표 위치로 이동
                for (Place place : places) {
                    if (place.contains(mouseX, mouseY)) {
                        moveToDest(place);
                        repaint();
                        break;
                    }
                }
            }
        });

        Timer timer = new Timer(15, e -> {
            repaint();
        });
        timer.start();
    }

    int centerX = 512;
    int centerY = 330;

    private void moveToCenter()
    {
        // 현재 위치가 목표 위치와 다를 경우 이동
        if (characterX != centerX || characterY != centerY) {
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
    }

    private void moveToDest(Place place) {
        // 목표 좌표 설정
        targetX = place.targetX;
        targetY = place.targetY;

        // 먼저 중앙으로 이동
        Timer centerTimer = new Timer(15, e -> {
            // 중앙으로 이동하는 동안
            if (characterX != centerX || characterY != centerY) {
                moveToCenter(); // 중앙으로 이동
                repaint();
            } else {
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

    // 이동 속도에 맞춰 캐릭터가 목표 좌표로 이동하도록 하는 메서드
    private void moveCharacter() {
        if (characterX != targetX || characterY != targetY) {
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

    // 이동 속도를 설정하는 메서드
    public void setMoveSpeed(int speed) {
        this.moveSpeed = speed;
    }

    // 이동 속도를 반환하는 메서드
    public int getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 캐릭터 이미지 그리기 (이미지의 중앙이 캐릭터 위치에 오도록 조정)
        int imageX = characterX - characterImg.getWidth(null) / 2;
        int imageY = characterY - characterImg.getHeight(null) / 2;
        g2d.drawImage(characterImg, imageX, imageY, null);

        //장애물 좌표 찾기 위한 코드
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));  // 두께 2배로 설정 (1.0f가 기본 두께)

//        //왼쪽 아이템
//        g2d.drawOval(50, 415, 45, 45); //1
//        g2d.drawOval(110, 395, 45, 45); //2
//        g2d.drawOval(170, 375, 45, 45); //3
//
//        //오른쪽 아이템
//        g2d.drawOval(820, 375, 45, 45); //1
//        g2d.drawOval(880, 395, 45, 45); //2
//        g2d.drawOval(940, 415, 45, 45); //3
//
//        //장소 1, 2, 3
//        g2d.drawOval(210, 170, 150, 150);
//        g2d.drawOval(450, 100, 150, 150);
//        g2d.drawOval(700, 170, 150, 150);
    }
}
