package Character;

import javax.swing.*;
import java.awt.*;

import GameManager.ClickEvent;
import Goods.PickDrop;
import GameManager.ItemManager;

public class Player extends Move implements ClickEvent {
    private Image characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
    private Image holdItemL = null; // 왼손
    private Image holdItemR = null; // 오른손
    private PickDrop pickDrop;

    public Player(ItemManager itemManager) {
        super(512, 330); // 초기 좌표 설정
        this.characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
        setOpaque(false);

        pickDrop = new PickDrop(this, itemManager);
    }

    @Override
    public Rectangle setBounds() {
        return new Rectangle(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getPriority() {
        return 1; // 높은 우선순위
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
    public boolean vistRoom = false;
    @Override
    public void onClick(Point clickPoint) {
        // 클릭된 좌표가 특정 Place 안에 있는지 확인
        for (Place place : getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                switch (place.getNum()) {
                    case 1 -> {
                        if(vistRoom) {
                            moveToDest(place, true, () -> {
                                pickDrop.handleItemClick(clickPoint); // 아이템 자동 집기
                                repaint();
                            });
                        }
                        else {
                            moveToDest(place, false, () -> {
                                pickDrop.handleItemClick(clickPoint); // 아이템 자동 집기
                                repaint();
                            });
                        }
                        vistRoom = false;
                    }
                    case 2 -> {
                        moveToDest(place, true, null);
                        vistRoom = true;
                    }
                    case 4 -> {
                        if(vistRoom) {
                            moveToDest(place, true, () -> {
                                // 아이템 자동 버리기
                                pickDrop.dropItem();
                            });
                        }
                        else {
                            moveToDest(place, false, () -> {
                                // 아이템 자동 버리기
                                pickDrop.dropItem();
                            });
                        }
                        vistRoom = false;
                    }
                    // 필요한 경우 다른 행동 추가
                    default -> {

                    }
                }
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
