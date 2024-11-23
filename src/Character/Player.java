package Character;

import javax.swing.*;
import java.awt.*;
import GameManager.ClickEvent;
import Goods.PickDrop;

public class Player extends Move implements ClickEvent {
    private Image characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
    private Image holdItemL = null; // 왼손에 들고 있는 아이템
    private Image holdItemR = null; // 오른손에 들고 있는 아이템
    private PickDrop pickDrop;

    public Player() {
        super(512, 330); // 초기 좌표 설정
        this.characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
        setOpaque(false);

        pickDrop = new PickDrop(this);
    }

    @Override
    public Rectangle setBounds() {
        return new Rectangle(0, 0, getWidth(), getHeight());
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
    public void onClick(Point clickPoint) {
        // 클릭된 좌표가 특정 Place 안에 있는지 확인
        for (Place place : getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                // 타겟 위치로 이동
                moveToDest(place);

                // 이동 완료 후 작업 처리
                Timer actionTimer = new Timer(15, actionEvent -> {
                    // 타겟 위치에 도착한 경우 동작 수행
                    if (characterX == place.getTargetX() && characterY == place.getTargetY()) {
                        ((Timer) actionEvent.getSource()).stop(); // 타이머 정지

                        switch (place.getNum()) {
                            case 1 -> {
                                // 아이템 자동 집기
                                pickDrop.handleItemClick(clickPoint);
                                repaint();
                            }
                            case 4 -> {
                                // 아이템 자동 버리기
                                pickDrop.dropItem();
                            }
                            // 필요한 경우 다른 행동 추가
                            default -> {
                            }
                        }
                    }
                });
                actionTimer.start();
                break;
            }
        }
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
