package Deco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import GameManager.FontManager;


public class GameInfo extends JPanel {
    private final Deco parent; // 부모 Deco 클래스 참조
    private final JButton nextButton;

    public GameInfo(Deco parent) {
        this.parent = parent;
        FontManager.loadCustomFont();
        JLabel textLabel = new JLabel(
                "<html><center>탑로더를 손님이 원하는 테마에 맞춰 꾸며주세요! <br/> 손님이 원하지 않은 파츠를 올리면 추가" +
                        " <br/><font color='#E8D241'>코인 ZERO!!</font></center></html>",
                SwingConstants.CENTER);
        textLabel.setOpaque(false);
        textLabel.setSize(700, 200);
        textLabel.setLocation(0, 0);
        textLabel.setFont(FontManager.loadFont(24f));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel commentPanel = new JPanel(null);
        commentPanel.setSize(700, 200);
        commentPanel.setLocation(
                (800 - commentPanel.getWidth()) / 2, 180);
        commentPanel.setBackground(new Color(255, 255, 252));
        commentPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 5, true));
        commentPanel.add(textLabel);

        ImageIcon originalIcon = new ImageIcon("assets/img/allow.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        nextButton = new JButton(resizedIcon);
        nextButton.setSize(100, 70);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setLocation(
                commentPanel.getWidth() - nextButton.getWidth() - 30,
                commentPanel.getHeight() - nextButton.getHeight() - 30);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.incrementClickCount(); // 부모 클래스의 clickCount 증가
                int clickCount = parent.getClickCount();

                if (clickCount == 1) {
                    textLabel.setText("<html><center>오늘 내가 만들 탑꾸는! <br/><font color='#2DA5DC'>"+
                            parent.randomDecoItem()+"</font></center></html>");
                } else if (clickCount == 2) {
                    textLabel.setText("<html><center>얼른 손님의 니즈에 맞춰 탑로더를 제작해주세요!</center></html>");
                    ImageIcon closeIcon = new ImageIcon("assets/img/x.png");
                    Image scaledCloseImage = closeIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    ImageIcon resizedCloseIcon = new ImageIcon(scaledCloseImage);
                    nextButton.setIcon(resizedCloseIcon);
                } else if (clickCount == 3) {
                    parent.layeredPane.remove(GameInfo.this); // GameInfo 패널 제거
                    parent.layeredPane.repaint(); // 화면 갱신
                }
            }
        });

        commentPanel.add(nextButton);
        setLayout(null);
        add(commentPanel);
    }
}
