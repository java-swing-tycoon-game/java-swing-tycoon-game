package Items;

import Scenes.Play;
import Character.bcAns;

import javax.swing.*;
import java.awt.*;

public class ItemPanel extends JPanel {
    public static ItemPanel instance;
    public static final boolean[] itemArray = {true, false, false, false}; // 기본값
    private static final String[] itemIcons = {
            "assets/img/item/itemCircle.png",
            "assets/img/item/sloganItem.png",
            "assets/img/item/tshirtItem.png",
            "assets/img/item/stickItem.png"
    };

    public ItemPanel() {
        setOpaque(false);
        instance = this;

        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // 아이템 간격 설정
        refreshItems();
    }
  
    public void refreshItems() {
        removeAll(); // 기존 아이템 제거
        addDefaultItem();

        itemArray[0] = !(itemArray[1] || itemArray[2] || itemArray[3]);

        // 티셔츠 아이템 자동 클릭
        if (itemArray[2]) {
            onItemClicked(2);
        }

        for (int i = 0; i < itemArray.length; i++) {
            if (itemArray[i]) {
                add(createItemButton(i));
            }
        }
        revalidate();
        repaint();
    }

    private JButton createItemButton(int index) {
        JButton itemButton = new JButton(new ImageIcon(itemIcons[index])); // 아이템 이미지를 버튼으로
        itemButton.setContentAreaFilled(false); // 버튼 배경 제거
        itemButton.setBorderPainted(false); // 버튼 테두리 제거
        itemButton.setFocusPainted(false); // 포커스 표시 제거
        itemButton.setToolTipText("아이템 " + (index + 1));

        // 버튼 클릭 이벤트 추가
        itemButton.addActionListener(e -> onItemClicked(index));
        return itemButton;
    }

    private void onItemClicked(int index) {
        // 클릭된 아이템에 대한 로직 추가
        if (index == 1) {
            System.out.println("슬로건 아이템 활성화!");
            bcAns.stop();
            itemArray[1] = false;
        } else if (index == 2) {
            System.out.println("티셔츠 아이템 활성화!");
            Tshirt.applyEffect(Play.player);
        } else if (index == 3) {
            System.out.println("스틱 아이템 활성화!");
            LightStick.use();
            itemArray[3] = false;
        }

        //refreshItems();
    }

    private void addDefaultItem() {
        JButton defaultItemButton = new JButton(new ImageIcon("assets/img/item.png"));
        defaultItemButton.setContentAreaFilled(false);
        defaultItemButton.setBorderPainted(false);
        defaultItemButton.setFocusPainted(false);
        defaultItemButton.setToolTipText("기본 아이템");

        // 기본 아이템 클릭 이벤트
        defaultItemButton.addActionListener(e -> System.out.println("기본 아이템 클릭됨!"));
        add(defaultItemButton);
    }
}
