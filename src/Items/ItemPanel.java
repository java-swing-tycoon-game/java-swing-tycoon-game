package Items;

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
        setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0)); // 아이템 간격 설정
       refreshItems();
    }
    public void refreshItems() {
        removeAll(); // 기존 아이템 제거
        addDefaultItem();

        itemArray[0] = !(itemArray[1] || itemArray[2] || itemArray[3]);

        for (int i = 0; i < itemArray.length; i++) {
            if (itemArray[i]) {
                JLabel itemLabel = new JLabel(new ImageIcon(itemIcons[i]));
                add(itemLabel);
            }
        }
        revalidate();
        repaint();
    }

    private void addDefaultItem() {
        JLabel defaultItemLabel = new JLabel(new ImageIcon("assets/img/item.png"));
        add(defaultItemLabel);
    }



}
