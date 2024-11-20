package Scenes;

import GameManager.FontManager;

import javax.swing.*;
import java.awt.*;

public class Buy extends JFrame {
    private String[] itemImgPath = {"assets/img/item/slogan.png", "assets/img/item/tshirts.png", "assets/img/item/stick.png", "assets/img/item/doll.png", "assets/img/item/bag.png", "assets/img/item/album.png"};

    public Buy() {
        showPopup();
    }

    private void showPopup() {
        // JDialog 설정
        JDialog buyPopup = new JDialog(this, "아이템을 구매해서 돈을 더 빨리 모으자!", true);
        buyPopup.setSize(950, 650);
        buyPopup.setLocationRelativeTo(null); // 화면 중앙에 배치
        buyPopup.setResizable(false);
        buyPopup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 배경 설정
        buyPopup.getContentPane().setBackground(Color.decode("#D5F2FF"));
        JLabel backgroundLabel = new JLabel(new ImageIcon("assets/img/panel/buy.png"));
        backgroundLabel.setLayout(null);

        // 아이템 패널
        JPanel itemPanel = new JPanel(new GridLayout(2, 3, 0, 0));
        itemPanel.setOpaque(false); // 배경 투명화
        itemPanel.setBounds(100, 150, 520, 340);

        // 아이템 이미지
        for (int i = 0; i < 6; i++) {
            // 아이템 박스
            JButton itemBox = new JButton(new ImageIcon("assets/img/panel/box.png"));
            itemBox.setLayout(new BorderLayout());
            itemBox.setBorderPainted(false);
            itemBox.setContentAreaFilled(false);

            Font font = FontManager.loadFont(20);
            JLabel itemLabel = new JLabel("100만원", new ImageIcon(itemImgPath[i]), SwingConstants.CENTER);
            itemLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            itemLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
            itemLabel.setFont(font);

            itemBox.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(itemBox);
        }

        JPanel rightPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.setBounds(590, 130, 300, 400);

        // 코인 타이틀 패널
        JPanel coinPanel = new JPanel(new BorderLayout());
        coinPanel.setOpaque(false);
        JLabel coinLabel = new JLabel(new ImageIcon("assets/img/coin.png"));
        coinPanel.add(coinLabel);

        // 소지 금액 패널
        JPanel haveCoinPanel = new JPanel(new FlowLayout());
        haveCoinPanel.setOpaque(false);

        JLabel coinImgLabel = new JLabel(new ImageIcon("assets/img/coinImage.png"));

        Font font = FontManager.loadFont(40);
        JLabel coinTxt = new JLabel("100만원");
        coinTxt.setFont(font);

        haveCoinPanel.add(coinImgLabel);
        haveCoinPanel.add(coinTxt);

        // 버튼 이미지
        JButton buyButton = new JButton(new ImageIcon("assets/img/button/buyButton.png"));
        buyButton.setBorderPainted(false);
        buyButton.setContentAreaFilled(false);

        JButton nextButton = new JButton(new ImageIcon("assets/img/button/nextButton.png"));
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);

        // 오른쪽 담당 패널에 코인과 버튼 추가
        rightPanel.add(coinPanel);
        rightPanel.add(haveCoinPanel);
        rightPanel.add(buyButton);
        rightPanel.add(nextButton);

        // 배경에 아이템과 버튼 추가
        backgroundLabel.add(itemPanel);
        backgroundLabel.add(rightPanel);

        buyPopup.getContentPane().add(backgroundLabel, BorderLayout.CENTER);

        buyPopup.setVisible(true);
    }

    public static void main(String[] args) {
        new Buy();
    }
}
