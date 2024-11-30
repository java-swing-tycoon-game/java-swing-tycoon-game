package Deco;

import javax.swing.*;
import java.awt.*;

public class PartsLayer extends JPanel {
    private final Deco deco;

    public PartsLayer(Deco deco) {
        this.deco = deco; // Deco 클래스 의존성 주입
        initializePartsLayer();
    }

    private void initializePartsLayer() {
        JPanel partsJPanel = new JPanel(null);
        partsJPanel.setSize(130, 500);
        partsJPanel.setLocation(deco.getWidth() - partsJPanel.getWidth() - 80, 100);
        partsJPanel.setOpaque(false);

        ImageIcon bluePartsIcon = new ImageIcon("assets/img/decoItem/blueParts.png");
        ImageIcon whitePartsIcon = new ImageIcon("assets/img/decoItem/whiteParts.png");
        ImageIcon pinkPartsIcon = new ImageIcon("assets/img/decoItem/pinkParts.png");

        JButton bluePartsButton = new JButton(bluePartsIcon);
        JButton whitePartsButton = new JButton(whitePartsIcon);
        JButton pinkPartsButton = new JButton(pinkPartsIcon);

        bluePartsButton.setFocusPainted(false);
        bluePartsButton.setBorderPainted(false);
        bluePartsButton.setContentAreaFilled(false);
        whitePartsButton.setFocusPainted(false);
        whitePartsButton.setBorderPainted(false);
        whitePartsButton.setContentAreaFilled(false);
        pinkPartsButton.setFocusPainted(false);
        pinkPartsButton.setBorderPainted(false);
        pinkPartsButton.setContentAreaFilled(false);

        bluePartsButton.setSize(120, 120);
        whitePartsButton.setSize(120, 120);
        pinkPartsButton.setSize(120, 130);

        bluePartsButton.setLocation(10, 20);
        whitePartsButton.setLocation(10, 140);
        pinkPartsButton.setLocation(10, 260);


        bluePartsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bluePartsButton.setLocation(0, 20);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deco.currentList[2] != 1) {
                    bluePartsButton.setLocation(10, 20);
                }
            }
        });

        whitePartsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                whitePartsButton.setLocation(0, 140);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deco.currentList[2] != 2) {
                    whitePartsButton.setLocation(10, 140);
                }
            }
        });

        pinkPartsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pinkPartsButton.setLocation(0, 260);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deco.currentList[2] != 0) {
                    pinkPartsButton.setLocation(10, 260);
                }
            }
        });

        // 버튼 클릭 이벤트 처리
        pinkPartsButton.addActionListener(e -> {
            deco.onCurrentListChanged(2, 0);
            if (deco.currentList[2] == 0) {
                pinkPartsButton.setLocation(0, 260);
                bluePartsButton.setLocation(10, 20);
                whitePartsButton.setLocation(10, 140);
            }
        });

        bluePartsButton.addActionListener(e -> {
            deco.onCurrentListChanged(2, 1);
            if (deco.currentList[2] == 1) {
                bluePartsButton.setLocation(0, 20);
                pinkPartsButton.setLocation(10, 260);
                whitePartsButton.setLocation(10, 140);
            }
        });

        whitePartsButton.addActionListener(e -> {
            deco.onCurrentListChanged(2, 2);
            if (deco.currentList[2] == 2) {
                whitePartsButton.setLocation(0, 140);
                bluePartsButton.setLocation(10, 20);
                pinkPartsButton.setLocation(10, 260);
            }
        });

        partsJPanel.add(pinkPartsButton);
        partsJPanel.add(whitePartsButton);
        partsJPanel.add(bluePartsButton);

        deco.layeredPane.add(partsJPanel, Integer.valueOf(2));
    }
}
