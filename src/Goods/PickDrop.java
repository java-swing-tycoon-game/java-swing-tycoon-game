package Goods;

import Character.Place;
import Character.Player;
import Character.Move;
import Scenes.Play;
import GameManager.ItemManager;
import Character.Npc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PickDrop extends JPanel {

    public PickDrop() {

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
        Play.player.setHoldItemL(item);
        Play.player.repaint();
    }

    public void pickUpItemRight(Image item) {
        Play.player.setHoldItemR(item);
        Play.player.repaint();
    }

    // 아이템 버리기 (양손 모두 버림)
    public void dropItem() {
        Play.player.setHoldItemL(null);
        Play.player.setHoldItemR(null);
        Play.player.repaint();
    }

    public void handleItemClick(Point clickPoint) {
        for (Place place : ItemManager.getPlaces()) {
            if (place.contains(clickPoint.x, clickPoint.y)) {
                String itemPath = ItemManager.getPathByImage(ItemManager.getItemForPlace(place));

                // 아이템이 존재하고 visible 상태인지 확인
                if (itemPath != null && ItemManager.isVisibleByPath(itemPath)) {
                    // 왼손이 비어 있으면 왼손에 들기
                    if (Play.player.getHoldItemL() == null) {
                        pickUpItemLeft(ItemManager.getImageByPath(itemPath));
                    }
                    // 오른손이 비어 있으면 오른손에 들기
                    else if (Play.player.getHoldItemR() == null) {
                        pickUpItemRight(ItemManager.getImageByPath(itemPath));
                    } else {
                        System.out.println("양손이 이미 차 있습니다.");
                    }
                    Play.player.repaint();
                    break;
                }
            }
        }
    }
}

