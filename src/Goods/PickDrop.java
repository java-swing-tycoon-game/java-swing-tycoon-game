/*
package Goods;

import Character.Place;
import Character.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class PickDrop extends JPanel {
    private ArrayList<Place> places;
    private HashMap<Place, Image> placeItemMap; // Place와 아이템 매핑
    private Player player; // 캐릭터 참조
    private Place currentPlace;

    public PickDrop(Player player) {
        this.player = player;
        places = Place.createPlaces();
        placeItemMap = new HashMap<>();

        // 아이템 이미지 로드
        Image[] itemImages = new Image[]{
                new ImageIcon("assets/img/item/album.png").getImage(),
                new ImageIcon("assets/img/item/bag.png").getImage(),
                new ImageIcon("assets/img/item/cup.png").getImage(),
                new ImageIcon("assets/img/item/doll.png").getImage(),
                new ImageIcon("assets/img/item/photoCard.png").getImage(),
                new ImageIcon("assets/img/item/popcorn.png").getImage()
        };

        // 각 Place에 아이템 매핑
        for (int i = 0; i < places.size(); i++) {
            if (i < itemImages.length) {
                placeItemMap.put(places.get(i), itemImages[i]);
            }
        }

       // 마우스 클릭 이벤트 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleItemClick(e.getPoint());
            }
        });
    }

    // 아이템 집기
    public void pickUpItemLeft(Image item) {
        player.setHoldItemL(item);
        player.repaint();
    }

    public void pickUpItemRight(Image item) {
        player.setHoldItemR(item);
        player.repaint();
    }

    // 아이템 버리기 (양손 모두 버림)
    public void dropItem() {
        player.setHoldItemL(null);
        player.setHoldItemR(null);
        player.repaint();
    }

    public void handleItemClick(Point clickPoint) {
        for (Place place : places) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                Image item = placeItemMap.get(place);  // 해당 장소에 매핑된 아이템을 가져옴
                if (item != null) {
                    // 왼손이 비어 있으면 왼손에 들기
                    if (player.getHoldItemL() == null) {
                        pickUpItemLeft(item);
                    }
                    // 오른손이 비어 있으면 오른손에 들기
                    else if (player.getHoldItemR() == null) {
                        pickUpItemRight(item);
                    }
                    else {
                        System.out.println("양손이 이미 차 있습니다.");
                    }
                    player.repaint();
                    break;
                }
            }
        }
    }
}

*/
