package Character;

import GameManager.ClickEvent;
import Goods.PickDrop;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MovePlayer implements ClickEvent {
    public Image characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();

    private Image walkingImg;
    private int walkingIndex = 0;

    private BufferedImage buffer;

    private Image holdItemL = null; // 왼손
    private Image holdItemR = null; // 오른손
    private final PickDrop pickDrop;

    public boolean isMoving = false; // 이동 중인지
    private int lastVisitedCase = -1; // 이전 방문 장소(Num)을 저장

    public Player() {
        super(512, 330); // 초기 좌표 설정
        setOpaque(false);
        walkingAnimation();
        pickDrop = new PickDrop();
    }

    public void setHoldItemL(Image item) {
        this.holdItemL = item;
    }
    public void setHoldItemR(Image item) {
        this.holdItemR = item;
    }

    public Image getHoldItemL() {
        return holdItemL;
    }
    public Image getHoldItemR() {
        return holdItemR;
    }

    public void imageChange (String path) {
        characterImg = new ImageIcon(path).getImage();
    }

    @Override
    public Rectangle setBounds() {
        return new Rectangle(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getPriority() {
        return 1;
    }

    ////// 클릭에 따라 player 이동 //////
    @Override
    public void onClick(Point clickPoint) {// 클릭된 좌표가 특정 Place 안에 있는지 확인
        for (Place place : getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                boolean viaCenter = lastVisitedCase != place.getNum();

                switch (place.getNum()) {
                    case 1 -> // 아이템
                            moveToDest(place, viaCenter, () -> {
                                pickDrop.handleItemClick(clickPoint); // 아이템 자동 집기
                                repaint();
                            });
                    case 4 -> // 쓰레기통
                            moveToDest(place, viaCenter, () -> {
                                pickDrop.dropItem(); // 아이템 자동 버리기
                                repaint();
                            });
                    default -> System.out.println("장소 디폴트 실행 중");
                }
                lastVisitedCase = place.getNum();
                break;
            }
        }}

    public void movePlayer(Point clickPoint, Runnable callback) {
        // 클릭된 좌표가 특정 Place 안에 있는지 확인
        for (Place place : getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                boolean viaCenter = lastVisitedCase != place.getNum();

                switch (place.getNum()) {
                    case 2 -> // 룸 3곳
                            moveToDest(place, viaCenter, () -> {
                                if (callback != null) {
                                    callback.run();
                                }
                            });
                    case 3 -> // 대기구역
                            moveToDest(place, viaCenter, () -> {
                                if (callback != null) {
                                    callback.run();
                                }
                            });
                    default -> System.out.println("요청 디폴트 실행 중");
                }
                lastVisitedCase = place.getNum();
                break;
            }
        }
    }

    @Override
    public void moveToDest(Place place, boolean viaCenter, Runnable callback) {
        // 이동 중이면 무시
        if (isMoving) { return; }

        isMoving = true; // 이동 시작
        super.moveToDest(place, viaCenter, () -> {
            isMoving = false; // 이동 후 완료
            if (callback != null) {
                callback.run();
            }
        });
    }

    private void walkingAnimation() {
            Timer walkingTimer = new Timer(200, _ -> {
            if(isMoving) {
                walkingIndex = (walkingIndex + 1) % 2;
            }
            else {
                walkingIndex = 2;
            }
            walkingImg = Npc.legImg[walkingIndex];
            repaint();
        });
        walkingTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buffer == null) {
            buffer = new BufferedImage(120, 150, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 캐릭터 이미지 그리기 (이미지의 중앙이 캐릭터 위치에 오도록 조정)
        int imageX = characterX - 60;
        int imageY = characterY - 75;

        g2d.drawImage(walkingImg, imageX, imageY, null);
        g2d.drawImage(characterImg, imageX, imageY, null);

        // 왼손에 그리기
        if (holdItemL != null) {
            int handX = characterX - 40; // 캐릭터 손 위치 조정
            int handY = characterY + 15;
            g2d.drawImage(holdItemL, handX, handY, 40, 40, null);
        }

        // 오른손에 그리기
        if (holdItemR != null) {
            int handX = characterX + 10; // 캐릭터 손 위치 조정
            int handY = characterY + 15;
            g2d.drawImage(holdItemR, handX, handY, 40, 40, null);
        }

        g2d.dispose();

        // 최종적으로 화면에 그리기
        g.drawImage(buffer, 0, 0, null);
    }
}
