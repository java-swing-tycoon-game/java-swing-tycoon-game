package GameManager;

import Character.Place;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private static ItemManager instance; // 싱글톤 인스턴스
    public static ArrayList<Image> itemImages; // 아이템 이미지

    private static ArrayList<Place> places; // Place 객체 리스트
    private static HashMap<Place, Image> placeItemMap; // Place와 아이템 매핑

    private static boolean[] visibleItems; // 보이나요
    private static final Map<String, Image> imageCache = new HashMap<>(); // 이미지 캐싱


    // 장소랑 아이템 연결 및 3개 보이게 세팅
    public ItemManager() {
        places = Place.createPlaces();
        itemImages = new ArrayList<>();
        setItemList();

        // Place와 아이템 매핑
        placeItemMap = new HashMap<>();
        for (int i = 0; i < itemImages.size(); i++) {
            placeItemMap.put(places.get(i+1), getItemImage(i));
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
        itemImages.add(loadImage("assets/img/item/cup.png"));
        itemImages.add(loadImage("assets/img/item/photoCard.png"));
        itemImages.add(loadImage("assets/img/item/popcorn.png"));
        itemImages.add(loadImage("assets/img/item/doll.png"));
        itemImages.add(loadImage("assets/img/item/bag.png"));
        itemImages.add(loadImage("assets/img/item/album.png"));
        itemImages.add(loadImage("assets/img/item/deco.png"));
    }

    // 이미지 로드 및 캐싱
    public Image loadImage(String path) {
        return imageCache.computeIfAbsent(path, p -> new ImageIcon(p).getImage());
    }

    // 싱글톤 인스턴스를 반환하는 메서드
    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    // 아이템 보여줄지 안 보여줄지
    public static void setVisibleItem(int index, boolean visible) {
            visibleItems[index] = visible;
    }

    // 특정 아이템이 보이는지 확인
    public static boolean getVisible(int index) {
        return visibleItems[index];
    }

    // Place에 매핑된 아이템 반환
    public static Image getItemForPlace(Place place) {
        return placeItemMap.get(place);
    }

    // Place 리스트 반환
    public static ArrayList<Place> getPlaces() {
        return places;
    }

    // 특정 아이템 반환
    public static Image getItemImage(int index) {
        if (index < 0 || index >= itemImages.size()) return null;

        // 캐싱된 이미지를 반환
        String path = "assets/img/item/" + getItemNameByIndex(index) + ".png";
        return imageCache.computeIfAbsent(path, p -> new ImageIcon(p).getImage());
    }

    // 아이템 이름 생성 메서드 추가 (예시)
    private static String getItemNameByIndex(int index) {
        switch (index) {
            case 0: return "cup";
            case 1: return "photoCard";
            case 2: return "popcorn";
            case 3: return "doll";
            case 4: return "bag";
            case 5: return "album";
            case 6: return "deco";
            default: throw new IllegalArgumentException("Invalid item index: " + index);
        }
    }
}
