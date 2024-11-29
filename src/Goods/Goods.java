package Goods;

import Character.Place;
import Character.Move;
import GameManager.ItemManager;

import javax.swing.*;
import java.awt.*;

public class Goods extends JPanel {
    private final ItemManager itemManager;

    public Goods(ItemManager itemManager) {
        this.itemManager = itemManager;
        System.out.println("Goods에서의 itemManager: " + itemManager);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < ItemManager.itemImages.size(); i++) {
            // visible 상태인 아이템만 화면에 표시
            if (itemManager.getVisible(i)) {
                // 맵에서 아이템 위치만 가져옴
                Place place = Move.places.get(i + 1);
                Image itemImage = ItemManager.itemImages.get(i);

                // 아이템 이미지를 Place 위치에 맞게 그림
                int drawX = place.getX() - place.getRadius();
                int drawY = place.getY() - place.getRadius() - 10;
                g.drawImage(itemImage, drawX, drawY, place.getRadius() * 2, place.getRadius() * 2, this);
            }
        }
    }
}

