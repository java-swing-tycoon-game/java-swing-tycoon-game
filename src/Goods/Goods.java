/*
package Goods;

import Character.Place;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Goods extends JPanel {

    private ArrayList<Place> places;
    private ArrayList<Image> itemImages;

    public Goods() {
        // Place 객체 생성
        places = Place.createPlaces();

        itemImages = new ArrayList<>();
        itemImages.add(new ImageIcon("assets/img/item/album.png").getImage()); //앨범
        itemImages.add(new ImageIcon("assets/img/item/bag.png").getImage()); //가방
        itemImages.add(new ImageIcon("assets/img/item/cup.png").getImage()); //컵
        itemImages.add(new ImageIcon("assets/img/item/doll.png").getImage()); //인형
        itemImages.add(new ImageIcon("assets/img/item/photoCard.png").getImage()); //포토카드
        itemImages.add(new ImageIcon("assets/img/item/popcorn.png").getImage()); //팝콘
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < 6; i++) {
            Place place = places.get(i);
            Image itemImage = itemImages.get(i); // 각 아이템의 이미지를 가져옴
            int drawX = place.getX() - place.getRadius(); // 이미지 중심 좌표로 조정
            int drawY = place.getY() - place.getRadius();
            g.drawImage(itemImage, drawX, drawY, place.getRadius() * 2, place.getRadius() * 2, this);
        }
    }
}

*/
