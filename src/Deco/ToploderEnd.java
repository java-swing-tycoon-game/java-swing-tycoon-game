package Deco;

import GameManager.FontManager;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ToploderEnd extends JPanel {
    private final Boolean gameResult;
    private final int[] selectedItems; // 올바른 배열 (선택된 항목)
    private final int frameWidth = 800; // 화면 크기
    private final int frameHeight = 600;
    private final Deco deco;


    public ToploderEnd(Boolean gameResult, int[] selectedItems, Deco deco) {
        this.gameResult = gameResult;
        this.selectedItems = selectedItems;
        this.deco = deco;
        setLayout(null);
        setOpaque(true);
        initialize();
    }

    private void initialize() {
        JLabel resultLabel = new JLabel(gameResult ? "축하합니다! 맞았습니다!" : "아쉽네요. 틀렸습니다.");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBounds(100, 50, 600, 50);
        resultLabel.setFont(FontManager.loadFont(gameResult ? 24f : 42f));
        resultLabel.setForeground(gameResult ? Color.BLACK : new Color(59, 59, 59));
        add(resultLabel);

        JLabel toploaderLabel = new JLabel(new ImageIcon("assets/img/decoItem/toploader.png"));
        int toploaderWidth = 300;
        int toploaderHeight = 340;
        int xPosition = (frameWidth - toploaderWidth) / 2;
        int yPosition = 150;
        toploaderLabel.setBounds(xPosition, yPosition, toploaderWidth, toploaderHeight);

        if (gameResult) {
            add(toploaderLabel);
            addCorrectLayers(xPosition, yPosition, toploaderWidth, toploaderHeight);
            addRandomPhotocard(xPosition, yPosition, toploaderWidth, toploaderHeight);
        }

        JButton exitButton = new JButton("종료");
        exitButton.setBounds((frameWidth - 100) / 2, 500, 100, 50);
        exitButton.addActionListener(e -> {
            if (deco != null) {
                deco.dispose();
            }
        });
        add(exitButton);
    }
    private void addRandomPhotocard(int xPosition, int yPosition, int toploaderWidth, int toploaderHeight) {
        Random random = new Random();
        int randomIndex = random.nextInt(6) + 1; // 1부터 6까지
        String photoCardPath = "assets/img/photocard/poka" + randomIndex + ".png";

        JLabel photoCardLabel = new JLabel(new ImageIcon(photoCardPath));
        int photoCardWidth = 200;
        int photoCardHeight = 300;
        int photoCardX = xPosition + (toploaderWidth - photoCardWidth) / 2;
        int photoCardY = yPosition;
        photoCardLabel.setBounds(photoCardX, photoCardY, photoCardWidth, photoCardHeight);
        add(photoCardLabel);
    }
    private void addCorrectLayers(int xPosition, int yPosition, int toploaderWidth, int toploaderHeight) {
        // Film 레이어 추가
        if (selectedItems[0] == 0) {
            JLabel heartLayer = new JLabel(new ImageIcon("assets/img/decoItem/heart.png"));
            heartLayer.setBounds(xPosition, yPosition, toploaderWidth, toploaderHeight);
            add(heartLayer);
        } else if (selectedItems[0] == 1) {
            JLabel starLayer = new JLabel(new ImageIcon("assets/img/decoItem/star.png"));
            starLayer.setBounds(xPosition, yPosition, toploaderWidth, toploaderHeight);
            add(starLayer);
        } else if (selectedItems[0] == 2) {
            JLabel auroraLayer = new JLabel(new ImageIcon("assets/img/decoItem/aurora.png"));
            auroraLayer.setBounds(xPosition, yPosition, toploaderWidth, toploaderHeight);
            add(auroraLayer);
        }

        // Color 레이어 추가
        if (selectedItems[1] == 0) {
            JLabel pinkLayer = new JLabel(new ImageIcon("assets/img/decoItem/pink.png"));
            pinkLayer.setBounds(xPosition - 15, yPosition - 10, toploaderWidth, toploaderHeight);
            add(pinkLayer);
        } else if (selectedItems[1] == 1) {
            JLabel blueLayer = new JLabel(new ImageIcon("assets/img/decoItem/blue.png"));
            blueLayer.setBounds(xPosition - 30, yPosition - 10, toploaderWidth, toploaderHeight);
            add(blueLayer);
        } else if (selectedItems[1] == 2) {
            JLabel yellowLayer = new JLabel(new ImageIcon("assets/img/decoItem/yellow.png"));
            yellowLayer.setBounds(xPosition , yPosition - 10, toploaderWidth, toploaderHeight);
            add(yellowLayer);
        }

        // Parts 레이어 추가
        if (selectedItems[2] == 0) {
            JLabel pinkPartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsP.png"));
            pinkPartsLayer.setBounds(xPosition, yPosition, toploaderWidth, toploaderHeight);
            add(pinkPartsLayer);
        } else if (selectedItems[2] == 1) {
            JLabel bluePartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsB.png"));
            bluePartsLayer.setBounds(xPosition, yPosition, toploaderWidth, toploaderHeight);
            add(bluePartsLayer);
        } else if (selectedItems[2] == 2) {
            JLabel whitePartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsW.png"));
            whitePartsLayer.setBounds(xPosition, yPosition, toploaderWidth, toploaderHeight);
            add(whitePartsLayer);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Component comp : getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getIcon() != null) {
                int toploaderWidth = 230;
                int xPosition = (getWidth() - toploaderWidth) / 2;
                comp.setBounds(xPosition, comp.getY(), toploaderWidth, 340);
            } else if (comp instanceof JButton) {
                int buttonWidth = 100;
                comp.setBounds((getWidth() - buttonWidth) / 2, comp.getY(), buttonWidth, 50);
            }
        }
    }
}
