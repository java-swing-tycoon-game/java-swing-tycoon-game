package Goods;

import Character.Place;
import GameManager.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Goods extends JPanel {

    private ItemManager itemManager;

    public Goods(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<Place> places = itemManager.getPlaces();
        ArrayList<Image> itemImages = itemManager.getItemImages();

        for (int i = 0; i < itemImages.size(); i++) {
            // visible 상태인 아이템만 화면에 표시
            if (itemManager.isVisible(i)) {
                Place place = places.get(i);
                Image itemImage = itemImages.get(i);

                // 아이템 이미지를 Place 위치에 맞게 그림
                int drawX = place.getX() - place.getRadius();
                int drawY = place.getY() - place.getRadius() - 10;
                g.drawImage(itemImage, drawX, drawY, place.getRadius() * 2, place.getRadius() * 2, this);
            }
        }
    }
}


