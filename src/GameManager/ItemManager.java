package GameManager;

import Character.Place;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager {
    private static ItemManager instance; // 싱글톤 인스턴스
    public static ArrayList<Image> itemImages; // 아이템 이미지

    private ArrayList<Place> places; // Place 객체 리스트
    private HashMap<Place, Image> placeItemMap; // Place와 아이템 매핑

    private boolean[] visibleItems; // 보이나요

    // 장소랑 아이템 연결 및 3개 보이게 세팅
    public ItemManager() {
        places = Place.createPlaces();
        setItemList();

        // Place와 아이템 매핑
        placeItemMap = new HashMap<>();
        for (int i = 0; i < itemImages.size(); i++) {
            placeItemMap.put(places.get(i+1), itemImages.get(i));
        }

        // 처음 3개 아이템만 보이게 스타트
        visibleItems = new boolean[itemImages.size()];

        System.out.println("itemImages 크기: " + itemImages.size());
        System.out.println("visibleItems 크기: " + visibleItems.length);


        for (int i = 0; i < 3; i++) {
            visibleItems[i] = true;
        }
    }

    // 아이템 이미지 리스트에 저장
    public void setItemList() {
        itemImages = new ArrayList<>();

        itemImages.add(new ImageIcon("assets/img/item/cup.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/photoCard.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/popcorn.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/doll.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/bag.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/album.png").getImage());
        itemImages.add(new ImageIcon("assets/img/item/deco.png").getImage());
    }

    // 싱글톤 인스턴스를 반환하는 메서드
    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    // 아이템 보여줄지 안 보여줄지
    public void setVisibleItem(int index, boolean visible) {
            visibleItems[index] = visible;
    }

    // 특정 아이템이 보이는지 확인
    public boolean getVisible(int index) {
        return visibleItems[index];
    }

    // Place에 매핑된 아이템 반환
    public Image getItemForPlace(Place place) {
        return placeItemMap.get(place);
    }

    // Place 리스트 반환
    public ArrayList<Place> getPlaces() {
        return places;
    }
}
