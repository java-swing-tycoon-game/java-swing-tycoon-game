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

    public void handleItemClick(Point clickPoint) {
        for (Place place : places) {
            // 클릭된 장소에 매핑된 아이템을 가져오기
            if (place.contains(clickPoint.x, clickPoint.y)) {
                Image item = placeItemMap.get(place);  // 해당 장소에 매핑된 아이템을 가져옴
                if (item != null) {
                    player.pickUpItem(item);  // 플레이어가 아이템을 집음
                    break;
                }
            }
        }
    }
}

*/
