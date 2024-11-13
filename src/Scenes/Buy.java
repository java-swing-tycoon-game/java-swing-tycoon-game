package Scenes;

import javax.swing.*;
import java.awt.*;

public class Buy extends JPanel {
    public Buy() {
        showPopup();
    }

    private JPanel popupPanel() {
        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(null);

        showBackground();
        showItem();
        showCoin();
        showButton();

        return popupPanel;
    }

    void showBackground() {

    }

    void showItem() {

    }

    void showCoin() {

    }

    void showButton() {

    }

    private void showPopup() {
        JDialog dialog = new JDialog();
        dialog.setTitle("아이템을 구매해서 돈을 더 빨리 모으자!");

        dialog.setSize(870, 570);
        dialog.getContentPane().add(popupPanel());
        dialog.setLocationRelativeTo(null); // 팝업이 화면 중앙에 나타나도록 설정
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new Buy();
    }
}