package Deco;

import javax.swing.*;
import java.awt.*;

public class ColorLayer extends JPanel {
    private final Deco deco;

    public ColorLayer(Deco deco) {
        this.deco = deco;
        initColorLayer();
    }

    private void initColorLayer() {
        JPanel colorJPanel = new JPanel(null);
        colorJPanel.setSize(250, 250);
        colorJPanel.setLocation((deco.getWidth() - colorJPanel.getWidth()) / 2, 20);
        colorJPanel.setOpaque(false);

        ImageIcon blueIcon = new ImageIcon("assets/img/decoItem/Pblue.png");
        ImageIcon yellowIcon = new ImageIcon("assets/img/decoItem/Pyellow.png");
        ImageIcon pinkIcon = new ImageIcon("assets/img/decoItem/Ppink.png");

        JButton blueButton = createButton(blueIcon, 50, 0);
        JButton yellowButton = createButton(yellowIcon, 100, 80);
        JButton pinkButton = createButton(pinkIcon, 0, 80);

        // 버튼별 마우스 리스너 추가
        addMouseListeners(blueButton, "assets/img/decoItem/BPblue.png", "assets/img/decoItem/Pblue.png", 1);
        addMouseListeners(yellowButton, "assets/img/decoItem/BPyellow.png", "assets/img/decoItem/Pyellow.png", 2);
        addMouseListeners(pinkButton, "assets/img/decoItem/BPpink.png", "assets/img/decoItem/Ppink.png", 0);

        // 버튼별 액션 리스너 추가
        addActionListeners(blueButton, yellowButton, pinkButton);

        colorJPanel.add(pinkButton);
        colorJPanel.add(yellowButton);
        colorJPanel.add(blueButton);

        deco.layeredPane.add(colorJPanel, Integer.valueOf(2));
    }

    private JButton createButton(ImageIcon icon, int x, int y) {
        JButton button = new JButton(icon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setSize(150, 150);
        button.setLocation(x, y);
        return button;
    }

    private void addMouseListeners(JButton button, String hoverIconPath, String defaultIconPath, int index) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setIcon(new ImageIcon(hoverIconPath));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deco.currentList[1] != index) {
                    button.setIcon(new ImageIcon(defaultIconPath));
                }
            }
        });
    }

    private void addActionListeners(JButton blueButton, JButton yellowButton, JButton pinkButton) {
        pinkButton.addActionListener(e -> {
            deco.onCurrentListChanged(1, 0);
            updateButtonIcons(pinkButton, yellowButton, blueButton,
                    "assets/img/decoItem/BPpink.png", "assets/img/decoItem/Pyellow.png", "assets/img/decoItem/Pblue.png");
        });

        blueButton.addActionListener(e -> {
            deco.onCurrentListChanged(1, 1);
            updateButtonIcons(blueButton, yellowButton, pinkButton,
                    "assets/img/decoItem/BPblue.png", "assets/img/decoItem/Pyellow.png", "assets/img/decoItem/Ppink.png");
        });

        yellowButton.addActionListener(e -> {
            deco.onCurrentListChanged(1, 2);
            updateButtonIcons(yellowButton, blueButton, pinkButton,
                    "assets/img/decoItem/BPyellow.png", "assets/img/decoItem/Pblue.png", "assets/img/decoItem/Ppink.png");
        });
    }

    private void updateButtonIcons(JButton activeButton, JButton button1, JButton button2,
                                   String activeIcon, String icon1, String icon2) {
        activeButton.setIcon(new ImageIcon(activeIcon));
        button1.setIcon(new ImageIcon(icon1));
        button2.setIcon(new ImageIcon(icon2));
    }
}
