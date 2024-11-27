package Goods;

import Character.Place;
import Character.Player;
import Character.Move;
import GameManager.ItemManager;
import Character.Npc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PickDrop extends JPanel {
    private final ItemManager itemManager;

    public PickDrop(ItemManager itemManager) {
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
        Npc.player.setHoldItemL(item);
        Npc.player.repaint();
    }

    public void pickUpItemRight(Image item) {
        Npc.player.setHoldItemR(item);
        Npc.player.repaint();
    }

    // 아이템 버리기 (양손 모두 버림)
    public void dropItem() {
        Npc.player.setHoldItemL(null);
        Npc.player.setHoldItemR(null);
        Npc.player.repaint();
    }

    public void handleItemClick(Point clickPoint) {
        for (Place place : itemManager.getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                Image item = itemManager.getItemForPlace(place);

                // 아이템이 존재하고 visible 상태인지 확인
                if (item != null && itemManager.getVisible(itemManager.getPlaces().indexOf(place)-1)) {
                    // 왼손이 비어 있으면 왼손에 들기
                    if (Npc.player.getHoldItemL() == null) {
                        pickUpItemLeft(item);
                    }
                    // 오른손이 비어 있으면 오른손에 들기
                    else if (Npc.player.getHoldItemR() == null) {
                        pickUpItemRight(item);
                    }
                    else {
                        System.out.println("양손이 이미 차 있습니다.");
                    }
                    Npc.player.repaint();
                }
                break;
            }
        }
    }
}

