package GameManager;

import Character.Place;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager {
    private ArrayList<Place> places; // Place 객체 리스트
    private ArrayList<Image> itemImages; // 모든 아이템 이미지
    private HashMap<Place, Image> placeItemMap; // Place와 아이템 매핑
    private boolean[] visibleItems; // 아이템 가시성 관리

    public ItemManager() {
        places = Place.createPlaces();

        // 아이템 이미지 로드
        itemImages = new ArrayList<>();
        itemImages.add(new ImageIcon("assets/img/item/cup.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/photoCard.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/popcorn.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/album.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/bag.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/doll.png").getImage());

        // Place와 아이템 매핑
        placeItemMap = new HashMap<>();
        for (int i = 0; i < places.size(); i++) {
            if (i < itemImages.size()) {
                placeItemMap.put(places.get(i), itemImages.get(i));
            }
        }

        // 초기 가시성 설정
        visibleItems = new boolean[itemImages.size()];
        for (int i = 0; i < 3; i++) {
            visibleItems[i] = true;
        }
    }

    // 아이템의 가시성 설정
    public void setVisibleItem(int index, boolean visible) {
            visibleItems[index] = visible;
    }

    // 특정 아이템이 보이는지 확인
    public boolean isVisible(int index) {
        return visibleItems[index];
    }

    // Place 리스트 반환
    public ArrayList<Place> getPlaces() {
        return places;
    }

    // Place에 매핑된 아이템 반환
    public Image getItemForPlace(Place place) {
        return placeItemMap.get(place);
    }

    // 모든 아이템 이미지 반환
    public ArrayList<Image> getItemImages() {
        return itemImages;
    }
}
