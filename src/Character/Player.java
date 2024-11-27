package Character;

import GameManager.ClickEvent;
import Goods.PickDrop;
import GameManager.ItemManager;

import javax.swing.*;
import java.awt.*;

public class Player extends Move implements ClickEvent {
    private final Image characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
    private Image holdItemL = null; // 왼손
    private Image holdItemR = null; // 오른손
    private PickDrop pickDrop;

    public Player(ItemManager itemManager) {
        super(512, 330); // 초기 좌표 설정
        setOpaque(false);

        pickDrop = new PickDrop(this, itemManager);
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

    @Override
    public Rectangle setBounds() {
        return new Rectangle(characterX-120/2, characterY-150/2, getWidth(), getHeight());
    }

    @Override
    public int getPriority() {
        return 1;
    }

    ////// 클릭에 따라 player 이동 //////
    // 이전 방문 장소(Num)을 저장
    private int lastVisitedCase = -1;

    @Override
    public void onClick(Point clickPoint) {
        // 클릭된 좌표가 특정 Place 안에 있는지 확인
        for (Place place : getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                boolean viaCenter = lastVisitedCase != place.getNum();

                System.out.println("다시 시작~!");
                System.out.println("직전 방문 case: " + (lastVisitedCase != -1 ? lastVisitedCase : "없음"));
                System.out.println("현재 case: " + place.getNum());

                switch (place.getNum()) {
                    case 1 -> { // 아이템
                        System.out.println(viaCenter);
                        moveToDest(place, viaCenter, () -> {
                                pickDrop.handleItemClick(clickPoint); // 아이템 자동 집기
                                repaint();
                            //checkNpcRequest(clickPoint);
                        });
                    }
                    case 2 -> { // 룸 3곳
                        moveToDest(place, viaCenter, () -> {
                            //checkNpcRequest(clickPoint); // NPC 요청 확인
                        });
                    }
                    case 3 -> { // 대기 구역
                        moveToDest(place, viaCenter, () -> {

                        });
                    }
                    case 4 -> { // 쓰레기통
                        moveToDest(place, viaCenter, () -> {
                            pickDrop.dropItem(); // 아이템 자동 버리기
                            repaint();
                        });
                    }
                    // 필요한 경우 다른 행동 추가
                    default -> {
                        System.out.println("디폴트 실행 중");
                    }
                }
                lastVisitedCase = place.getNum();
                System.out.println("업데이트 된 방문 장소: " + lastVisitedCase);
                break;
            }
        }
    }

//    // NPC 요청 확인 메서드
//    private void checkNpcRequest(Point clickPoint) {
//        for (Npc npc : ClickManager.getNpcList()) { // 등록된 NPC 리스트 가져오기
//            if (npc.getBounds().contains(clickPoint)) { // 클릭된 좌표가 NPC 영역인지 확인
//                System.out.println("NPC 요청을 확인합니다.");
//                npc.onClick(clickPoint); // NPC 요청 처리
//            }
//        }
//    }

    private boolean isMoving = false; // 이동 중인지 여부

    @Override
    public void moveToDest(Place place, boolean viaCenter, Runnable callback) {
        if (isMoving) {
            System.out.println("Player가 이미 이동 중입니다. 이동 중단.");
            return; // 이동 중이면 중단
        }

        isMoving = true; // 이동 시작
        super.moveToDest(place, viaCenter, () -> {
            isMoving = false; // 이동 완료
            if (callback != null) {
                callback.run();
            }
        });
    }

    public boolean getActive() {
        return !isMoving; // 이동 중이 아닐 때만 활성 상태로 간주
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
    }
}
