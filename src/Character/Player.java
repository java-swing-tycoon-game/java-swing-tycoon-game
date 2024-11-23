package Character;

import javax.swing.*;
import java.awt.*;
import GameManager.ClickEvent;
import Goods.PickDrop;

public class Player extends Move implements ClickEvent {
    private Image characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
    private Image holdItem = null; // 캐릭터가 들고 있는 아이템
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

    // 아이템 들기
    public void pickUpItem(Image item) {
        this.holdItem = item;
        repaint();
    }

    // 아이템 내려놓기
    public void dropItem() {
        this.holdItem = null;
        repaint();
    }

    @Override
    public void onClick(Point clickPoint) {
        // 클릭된 좌표가 특정 Place 안에 있는지 확인하고 이동
        for (Place place : getPlaces()) {
                if (place.contains(clickPoint.x, clickPoint.y)) {
                    if(characterX != place.getTargetX() && characterY != place.getTargetY()) {
                        System.out.println("클릭되었습니다.");
                        moveToDest(place);
                         break;
                }
                    else if(characterX == place.getTargetX() && characterY == place.getTargetY()) {
                        pickDrop.handleItemClick(clickPoint);
                    }
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

        if (holdItem != null) {
            int handX = characterX + 10; // 캐릭터 손 위치 조정
            int handY = characterY + 15;
            g2d.drawImage(holdItem, handX, handY, 40, 40, null);
        }
     }
}
