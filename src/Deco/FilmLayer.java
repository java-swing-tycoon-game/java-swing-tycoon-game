package Deco;

import javax.swing.*;

public class FilmLayer extends JPanel {
    private final Deco deco; // Deco 클래스 의존성 주입

    public FilmLayer(Deco deco) {
        this.deco = deco; // Deco 클래스 인스턴스를 받음
        initializeFilmLayer();
    }

    private void initializeFilmLayer() {
        JPanel filmJPanel = new JPanel(null);
        filmJPanel.setSize(200, 400);
        filmJPanel.setLocation(50, 130); // FilmLayer 위치 설정
        filmJPanel.setOpaque(false);

        ImageIcon starIcon = new ImageIcon("assets/img/decoItem/starFilm.png");
        ImageIcon heartIcon = new ImageIcon("assets/img/decoItem/heartFilm.png");
        ImageIcon auroraIcon = new ImageIcon("assets/img/decoItem/auroraFilm.png");

        JButton starButton = createButton(starIcon, 30, 0);
        JButton heartButton = createButton(heartIcon, 15, 80);
        JButton auroraButton = createButton(auroraIcon, 0, 160);

        // 마우스 이벤트 및 버튼 클릭 이벤트 추가
        addMouseListeners(starButton, heartButton, auroraButton);

        filmJPanel.add(starButton);
        filmJPanel.add(heartButton);
        filmJPanel.add(auroraButton);

        deco.layeredPane.add(filmJPanel, Integer.valueOf(2));
    }

    private JButton createButton(ImageIcon icon, int x, int y) {
        JButton button = new JButton(icon);
        button.setSize(150, 220);
        button.setLocation(x, y);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private void addMouseListeners(JButton starButton, JButton heartButton, JButton auroraButton) {
        starButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                starButton.setSize(170, 240);
                starButton.setLocation(30, -10);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deco.currentList[0] != 1) {
                    starButton.setSize(150, 220);
                    starButton.setLocation(30, 0);
                }
            }
        });

        heartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                heartButton.setSize(170, 240);
                heartButton.setLocation(15, 70);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deco.currentList[0] != 0) {
                    heartButton.setSize(150, 220);
                    heartButton.setLocation(15, 80);
                }
            }
        });

        auroraButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                auroraButton.setSize(170, 240);
                auroraButton.setLocation(0, 150);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deco.currentList[0] != 2) {
                    auroraButton.setSize(150, 220);
                    auroraButton.setLocation(0, 160);
                }
            }
        });

        heartButton.addActionListener(e -> {
            deco.onCurrentListChanged(0, 0);
            resetButtons(starButton, heartButton, auroraButton, 0);
        });

        starButton.addActionListener(e -> {
            deco.onCurrentListChanged(0, 1);
            resetButtons(starButton, heartButton, auroraButton, 1);
        });

        auroraButton.addActionListener(e -> {
            deco.onCurrentListChanged(0, 2);
            resetButtons(starButton, heartButton, auroraButton, 2);
        });
    }

    private void resetButtons(JButton starButton, JButton heartButton, JButton auroraButton, int activeIndex) {
        if (activeIndex == 0) {
            heartButton.setSize(170, 240);
            heartButton.setLocation(15, 70);
            starButton.setSize(150, 220);
            starButton.setLocation(30, 0);
            auroraButton.setSize(150, 220);
            auroraButton.setLocation(0, 160);
        } else if (activeIndex == 1) {
            starButton.setSize(170, 240);
            starButton.setLocation(30, -10);
            heartButton.setSize(150, 220);
            heartButton.setLocation(15, 80);
            auroraButton.setSize(150, 220);
            auroraButton.setLocation(0, 160);
        } else if (activeIndex == 2) {
            auroraButton.setSize(170, 240);
            auroraButton.setLocation(0, 150);
            starButton.setSize(150, 220);
            starButton.setLocation(30, 0);
            heartButton.setSize(150, 220);
            heartButton.setLocation(15, 80);
        }
    }
}
