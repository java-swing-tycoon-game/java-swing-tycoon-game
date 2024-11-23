package Goods;

import Character.Place;
import Character.Player;
import GameManager.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PickDrop extends JPanel {
    private Player player; // 캐릭터 참조
    private ItemManager itemManager;

    public PickDrop(Player player, ItemManager itemManager) {
        this.player = player;
        this.itemManager = itemManager;

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
        for (Place place : itemManager.getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                Image item = itemManager.getItemForPlace(place);

                // 아이템이 존재하고 visible 상태인지 확인
                if (item != null && itemManager.isVisible(itemManager.getPlaces().indexOf(place))) {
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
                }
                break;
            }
        }
    }
}

