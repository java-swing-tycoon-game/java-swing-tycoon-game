package GameManager;

import Character.Place;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private static ItemManager instance; // 싱글톤 인스턴스
    private static final Map<String, Image> imageMap = new HashMap<>(); // 경로와 이미지 매핑
    private static final List<String> itemPaths = new ArrayList<>(); // 경로 리스트
    private static boolean[] visibleItems; // 가시성 배열
    private static List<Place> places; // Place 객체 리스트

    // 초기화
    public ItemManager() {
        places = Place.createPlaces();
        setItemList();
        visibleItems = new boolean[itemPaths.size()];

        // 처음 3개 아이템만 보이게 설정
        for (int i = 0; i < 3; i++) {
            visibleItems[i] = true;
        }
    }

    // 아이템 이미지 경로 및 리스트 초기화
    public void setItemList() {
        addItem("assets/img/item/cup.png");
        addItem("assets/img/item/photoCard.png");
        addItem("assets/img/item/popcorn.png");
        addItem("assets/img/item/doll.png");
        addItem("assets/img/item/bag.png");
        addItem("assets/img/item/album.png");
        addItem("assets/img/item/deco.png");
    }

    // 아이템 추가 메서드
    private void addItem(String path) {
        Image image = new ImageIcon(path).getImage();
        imageMap.put(path, image);
        itemPaths.add(path);
    }

    // 싱글톤 인스턴스 반환
    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    // 특정 경로에 해당하는 이미지 반환
    public static Image getImageByPath(String path) {
        return imageMap.get(path);
    }

    // 특정 이미지에 해당하는 경로 반환
    public static String getPathByImage(Image image) {
        return imageMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(image))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    // 가시성 설정
    public static void setVisibleItem(int index, boolean visible) {
        visibleItems[index] = visible;
    }

    // 특정 아이템이 보이는지 확인
    public static boolean getVisible(int index) {
        return visibleItems[index];
    }

    // 특정 인덱스의 아이템 경로 반환
    public static String getItemPath(int index) {
        return (index >= 0 && index < itemPaths.size()) ? itemPaths.get(index) : null;
    }

    // 특정 경로의 가시성 확인
    public static boolean isVisibleByPath(String path) {
        int index = itemPaths.indexOf(path);
        return index >= 0 && visibleItems[index];
    }

    // Place 리스트 반환
    public static List<Place> getPlaces() {
        return places;
    }

    // Place에 매핑된 아이템 반환
    public static Image getItemForPlace(Place place) {
        int index = places.indexOf(place) - 1; // Place 리스트에서의 인덱스
        return (index >= 0 && index < itemPaths.size()) ? getImageByPath(getItemPath(index)) : null;
    }
}
