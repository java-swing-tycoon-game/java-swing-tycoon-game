package Deco;

import javax.swing.*;

public class TopLoader extends JPanel {
    private final Deco deco;

    public TopLoader(Deco deco) {
        this.deco = deco; // Deco 클래스와 연동
        setLayout(null);
        setOpaque(false);
        initialize();
    }

    private void initialize() {
        JLabel toploaderLabel = new JLabel(new ImageIcon("assets/img/decoItem/toploader.png"));
        int frameWidth = deco.getWidth();
        int frameHeight = deco.getHeight();
        int xPosition = (frameWidth - 600) / 2;
        int yPosition = (frameHeight - 600) / 2;

        toploaderLabel.setBounds(xPosition, yPosition, 600, 780 );
        add(toploaderLabel, Integer.valueOf(1));
        ImageIcon endIcon = new ImageIcon("assets/img/decoItem/endBtn.png");
        JButton endButton = new JButton(endIcon);
        endButton.setSize(200, 100);
        endButton.setLocation((deco.getWidth() - endButton.getWidth()) / 2, deco.getHeight() - endButton.getHeight() - 60);
        endButton.setFocusPainted(false);
        endButton.setBorderPainted(false);
        endButton.setContentAreaFilled(false);

        endButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                endButton.setIcon(new ImageIcon("assets/img/decoItem/BendBtn.png"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                endButton.setIcon(new ImageIcon("assets/img/decoItem/endBtn.png"));
            }
        });
        add(endButton, Integer.valueOf(10));
        endButton.addActionListener(e -> {

            deco.gameResult = deco.gameEnd();

            deco.ending();

        });
    }


    public void updateLayers() {
        removeAll();
        initialize();

        int frameWidth = deco.getWidth();
        int frameHeight = deco.getHeight();
        int xPosition = (frameWidth - 600) / 2;
        int yPosition = (frameHeight - 600) / 2;


        if (deco.currentList[0] == 0) {
            JLabel heartLayer = new JLabel(new ImageIcon("assets/img/decoItem/heart.png"));
            heartLayer.setBounds(xPosition, yPosition, 600, 600 + 180);
            add(heartLayer, Integer.valueOf(5));
        } else if (deco.currentList[0] == 1) {
            JLabel starLayer = new JLabel(new ImageIcon("assets/img/decoItem/star.png"));
            starLayer.setBounds(xPosition, yPosition, 600, 600 + 180);
            add(starLayer, Integer.valueOf(5));
        } else if (deco.currentList[0] == 2) {
            JLabel auroraLayer = new JLabel(new ImageIcon("assets/img/decoItem/aurora.png"));
            auroraLayer.setBounds(xPosition, yPosition, 600, 600 + 180);
            add(auroraLayer, Integer.valueOf(5));
        }

        // 색상 레이어
        if (deco.currentList[1] == 0) {
            JLabel pinkLayer = new JLabel(new ImageIcon("assets/img/decoItem/pink.png"));
            pinkLayer.setBounds(xPosition - 5, yPosition - 10, 600, frameHeight + 180);
            add(pinkLayer, Integer.valueOf(4));
        } else if (deco.currentList[1] == 1) {
            JLabel blueLayer = new JLabel(new ImageIcon("assets/img/decoItem/blue.png"));
            blueLayer.setBounds(xPosition - 5, yPosition - 10, 600, frameHeight + 180);
            add(blueLayer, Integer.valueOf(4));
        } else if (deco.currentList[1] == 2) {
            JLabel yellowLayer = new JLabel(new ImageIcon("assets/img/decoItem/yellow.png"));
            yellowLayer.setBounds(xPosition - 5, yPosition - 10, 600, frameHeight + 180);
            add(yellowLayer, Integer.valueOf(4));
        }

        // 파츠 레이어
        if (deco.currentList[2] == 0) {
            JLabel pinkPartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsP.png"));
            pinkPartsLayer.setBounds(xPosition, yPosition, 580, frameHeight + 180);
            add(pinkPartsLayer, Integer.valueOf(6));
        } else if (deco.currentList[2] == 1) {
            JLabel bluePartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsB.png"));
            bluePartsLayer.setBounds(xPosition, yPosition, 600, frameHeight + 180);
            add(bluePartsLayer, Integer.valueOf(6));
        } else if (deco.currentList[2] == 2) {
            JLabel whitePartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsW.png"));
            whitePartsLayer.setBounds(xPosition, yPosition, 600, frameHeight + 180);
            add(whitePartsLayer, Integer.valueOf(6));
        }

        revalidate();
        repaint();
    }
}
