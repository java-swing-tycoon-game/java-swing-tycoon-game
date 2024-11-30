package Scenes;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GameManager.FontManager;

public class Deco extends JFrame {
    public Boolean gameResult = null;
    private JButton nextButton;
    public static Font customFont;
    private int clickCount = 0;
    private final JLayeredPane layeredPane;
    private final int[] selectedItems = new int[3];
    private final int[] currentList = new int[3];
    private final String[] films = { "사랑하는 마음이 담긴", "내 스타를 위한", "하늘에 수놓은 비단같은" };
    private final String[] colors = { "벚꽃", "푸른", "레몬" };
    private final String[] themes = { "공주", "바다", "진주" };

    public Deco() {
        loadCustomFont();
        Arrays.fill(currentList, 3);

        setTitle("탑로더를 꾸며보아요!");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon originalIcon = new ImageIcon("assets/img/decoItem/Frame2.png");
        Image backgroundImage = originalIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(backgroundPanel, Integer.valueOf(0));

        backgroundPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                // 게임설명 하는 동안 게임 클릭 방지
                if (clickCount < 3) {
                    evt.consume();
                }
            }
        });

        gameInfo();

        setVisible(true);
    }

    private void gameInfo() {
        // 게임설명창
        JLabel textLabel = new JLabel(
                "<html><center>탑로더를 손님이 원하는 테마에 맞춰 꾸며주세요! <br/> 손님이 원하지 않은 파츠를 올리면 추가" +
                        " <br/><font color='#E8D241'>코인 ZERO!!</font></center></html>",
                SwingConstants.CENTER);
        textLabel.setOpaque(false);
        textLabel.setSize(700, 200);
        textLabel.setLocation(0, 0);
        textLabel.setFont(customFont.deriveFont(24f));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel commentPanel = new JPanel(null);

        commentPanel.setSize(700, 200);
        commentPanel.setLocation(
                (getWidth() - commentPanel.getWidth()) / 2,
                180);
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

        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            // 마우스 hover 일때 버튼 살짝 커지게
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (clickCount != 2) {
                    nextButton.setSize(120, 84);
                    ImageIcon originalIcon = new ImageIcon("assets/img/allow.png");
                    Image scaledImage = originalIcon.getImage().getScaledInstance(120, 84, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(scaledImage);
                    nextButton.setLocation(
                            commentPanel.getWidth() - nextButton.getWidth() - 20,
                            commentPanel.getHeight() - nextButton.getHeight() - 20);
                    nextButton.setIcon(resizedIcon);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (clickCount != 2) {
                    nextButton.setSize(100, 70);
                    ImageIcon originalIcon = new ImageIcon("assets/img/allow.png");
                    Image scaledImage = originalIcon.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(scaledImage);
                    nextButton.setLocation(
                            commentPanel.getWidth() - nextButton.getWidth() - 30,
                            commentPanel.getHeight() - nextButton.getHeight() - 30);
                    nextButton.setIcon(resizedIcon);
                }

            }
        });

        nextButton.addActionListener(new ActionListener() {
            // 설명 다음 넘길때마다 글자랑 버튼 모양 바뀌게 마지막만 x 버튼임
            @Override
            public void actionPerformed(ActionEvent e) {
                clickCount++;

                if (clickCount == 1) {
                    String randomItem = randomDecoItem();
                    textLabel.setText("<html><center>오늘 내가 만들 탑꾸는! <br/><font color='#2DA5DC'>" + randomItem
                            + "</center></html>");
                } else if (clickCount == 2) {
                    textLabel.setText("<html><center>얼른 손님의 니즈에 맞춰 탑로더를 제작해주세요!</center></html>");
                    ImageIcon closeIcon = new ImageIcon("assets/img/x.png");
                    Image scaledCloseImage = closeIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    ImageIcon resizedCloseIcon = new ImageIcon(scaledCloseImage);
                    nextButton.setIcon(resizedCloseIcon);
                } else if (clickCount == 3) {
                    commentPanel.setVisible(false);
                    decoItem();
                    filmLayer();
                    colorLayer();
                    partsLayer();
                    game();
                }
            }
        });

        commentPanel.add(nextButton);

        layeredPane.add(commentPanel, Integer.valueOf(2));

    }

    private String randomDecoItem() {
        Random random = new Random();

        selectedItems[0] = random.nextInt(films.length);
        selectedItems[1] = random.nextInt(colors.length);
        selectedItems[2] = random.nextInt(themes.length);

        return films[selectedItems[0]] + " " + colors[selectedItems[1]] + " " + themes[selectedItems[2]];
    }

    private void filmLayer() {
        JPanel filmJPanel = new JPanel(null);
        filmJPanel.setSize(200, 400);
        filmJPanel.setLocation(50, 130);
        filmJPanel.setOpaque(false);

        ImageIcon starIcon = new ImageIcon("assets/img/decoItem/starFilm.png");
        ImageIcon heartIcon = new ImageIcon("assets/img/decoItem/heartFilm.png");
        ImageIcon auroraIcon = new ImageIcon("assets/img/decoItem/auroraFilm.png");

        JButton starButton = new JButton(starIcon);
        JButton heartButton = new JButton(heartIcon);
        JButton auroraButton = new JButton(auroraIcon);

        starButton.setFocusPainted(false);
        starButton.setBorderPainted(false);
        starButton.setContentAreaFilled(false);
        heartButton.setFocusPainted(false);
        heartButton.setBorderPainted(false);
        heartButton.setContentAreaFilled(false);
        auroraButton.setFocusPainted(false);
        auroraButton.setBorderPainted(false);
        auroraButton.setContentAreaFilled(false);

        starButton.setSize(150, 220);
        heartButton.setSize(150, 220);
        auroraButton.setSize(150, 220);

        starButton.setLocation(30, 0);
        heartButton.setLocation(15, 80);
        auroraButton.setLocation(0, 160);

        starButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                starButton.setSize(170, 240);
                starButton.setLocation(30, -10);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (currentList[0] != 1) {
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
                if (currentList[0] != 0) {
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
                if (currentList[0] != 2) {
                    auroraButton.setSize(150, 220);
                    auroraButton.setLocation(0, 160);
                }
            }
        });

        heartButton.addActionListener(e -> {
            onCurrentListChanged(0, 0);
            if (currentList[0] == 0) {
                heartButton.setSize(170, 240);
                heartButton.setLocation(15, 70);
                starButton.setSize(150, 220);
                starButton.setLocation(30, 0);
                auroraButton.setSize(150, 220);
                auroraButton.setLocation(0, 160);
            }
        });

        starButton.addActionListener(e -> {
            onCurrentListChanged(0, 1);
            if (currentList[0] == 1) {
                starButton.setSize(170, 240);
                starButton.setLocation(30, -10);
                heartButton.setSize(150, 220);
                heartButton.setLocation(15, 80);
                auroraButton.setSize(150, 220);
                auroraButton.setLocation(0, 160);
            }
        });

        auroraButton.addActionListener(e -> {
            onCurrentListChanged(0, 2);
            if (currentList[0] == 2) {
                auroraButton.setSize(170, 240);
                auroraButton.setLocation(0, 150);
                starButton.setSize(150, 220);
                starButton.setLocation(30, 0);
                heartButton.setSize(150, 220);
                heartButton.setLocation(15, 80);
            }
        });

        filmJPanel.add(auroraButton);
        filmJPanel.add(heartButton);
        filmJPanel.add(starButton);

        layeredPane.add(filmJPanel, Integer.valueOf(1));
    }

    private void partsLayer() {
        JPanel partsJPanel = new JPanel(null);
        partsJPanel.setSize(130, 500);
        partsJPanel.setLocation(getWidth() - partsJPanel.getWidth() - 80, 100);
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
                if (currentList[2] != 1) {
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
                if (currentList[2] != 2) {
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
                if (currentList[2] != 0) {
                    pinkPartsButton.setLocation(10, 260);
                }
            }
        });

        pinkPartsButton.addActionListener(e -> {
            onCurrentListChanged(2, 0);
            if (currentList[2] == 0) {
                pinkPartsButton.setLocation(0, 260);
                bluePartsButton.setLocation(10, 20);
                whitePartsButton.setLocation(10, 140);
            }
        });

        bluePartsButton.addActionListener(e -> {
            onCurrentListChanged(2, 1);
            if (currentList[2] == 1) {
                bluePartsButton.setLocation(0, 20);
                pinkPartsButton.setLocation(10, 260);
                whitePartsButton.setLocation(10, 140);
            }
        });

        whitePartsButton.addActionListener(e -> {
            onCurrentListChanged(2, 2);
            if (currentList[2] == 2) {
                whitePartsButton.setLocation(0, 140);
                bluePartsButton.setLocation(10, 20);
                pinkPartsButton.setLocation(10, 260);
            }
        });

        partsJPanel.add(pinkPartsButton);
        partsJPanel.add(whitePartsButton);
        partsJPanel.add(bluePartsButton);

        layeredPane.add(partsJPanel, Integer.valueOf(1));
    }

    private void colorLayer() {
        JPanel colorJPanel = new JPanel(null);
        colorJPanel.setSize(250, 250);
        colorJPanel.setLocation((getWidth() - colorJPanel.getWidth()) / 2, 20);
        colorJPanel.setOpaque(false);

        ImageIcon blueIcon = new ImageIcon("assets/img/decoItem/Pblue.png");
        ImageIcon yellowIcon = new ImageIcon("assets/img/decoItem/Pyellow.png");
        ImageIcon pinkIcon = new ImageIcon("assets/img/decoItem/Ppink.png");

        JButton blueButton = new JButton(blueIcon);
        JButton yellowButton = new JButton(yellowIcon);
        JButton pinkButton = new JButton(pinkIcon);

        blueButton.setFocusPainted(false);
        blueButton.setBorderPainted(false);
        blueButton.setContentAreaFilled(false);
        yellowButton.setFocusPainted(false);
        yellowButton.setBorderPainted(false);
        yellowButton.setContentAreaFilled(false);
        pinkButton.setFocusPainted(false);
        pinkButton.setBorderPainted(false);
        pinkButton.setContentAreaFilled(false);

        blueButton.setSize(150, 150);
        yellowButton.setSize(150, 150);
        pinkButton.setSize(150, 150);

        blueButton.setLocation(50, 0);
        yellowButton.setLocation(100, 80);
        pinkButton.setLocation(0, 80);

        blueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                blueButton.setIcon(new ImageIcon("assets/img/decoItem/BPblue.png"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (currentList[1] != 1) {
                    blueButton.setIcon(new ImageIcon("assets/img/decoItem/Pblue.png"));
                }
            }
        });

        yellowButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                yellowButton.setIcon(new ImageIcon("assets/img/decoItem/BPyellow.png"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (currentList[1] != 2) {
                    yellowButton.setIcon(new ImageIcon("assets/img/decoItem/Pyellow.png"));
                }
            }
        });

        pinkButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pinkButton.setIcon(new ImageIcon("assets/img/decoItem/BPpink.png"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (currentList[1] != 0) {
                    pinkButton.setIcon(new ImageIcon("assets/img/decoItem/Ppink.png"));
                }
            }
        });

        pinkButton.addActionListener(e -> {
            onCurrentListChanged(1, 0);
            if (currentList[1] == 0) {
                pinkButton.setIcon(new ImageIcon("assets/img/decoItem/BPpink.png"));
                yellowButton.setIcon(new ImageIcon("assets/img/decoItem/Pyellow.png"));
                blueButton.setIcon(new ImageIcon("assets/img/decoItem/Pblue.png"));
            }
        });

        blueButton.addActionListener(e -> {
            onCurrentListChanged(1, 1);
            if (currentList[1] == 1) {
                blueButton.setIcon(new ImageIcon("assets/img/decoItem/BPblue.png"));
                yellowButton.setIcon(new ImageIcon("assets/img/decoItem/Pyellow.png"));
                pinkButton.setIcon(new ImageIcon("assets/img/decoItem/Ppink.png"));
            }
        });

        yellowButton.addActionListener(e -> {
            onCurrentListChanged(1, 2);
            if (currentList[1] == 2) {
                yellowButton.setIcon(new ImageIcon("assets/img/decoItem/BPyellow.png"));
                blueButton.setIcon(new ImageIcon("assets/img/decoItem/Pblue.png"));
                pinkButton.setIcon(new ImageIcon("assets/img/decoItem/Ppink.png"));
            }
        });

        colorJPanel.add(pinkButton);
        colorJPanel.add(yellowButton);
        colorJPanel.add(blueButton);

        layeredPane.add(colorJPanel, Integer.valueOf(1));
    }

    private void decoItem() {
        JPanel decoJPanel = new JPanel(null);
        decoJPanel.setSize(600, 50);
        decoJPanel.setLocation(0, 0);
        decoJPanel.setOpaque(false);

        JLabel textLabel = new JLabel(
                "\"" + films[selectedItems[0]] + "\" \"" +
                        colors[selectedItems[1]] + "\" \"" +
                        themes[selectedItems[2]] + "\"",
                SwingConstants.CENTER);
        textLabel.setOpaque(false);
        textLabel.setSize(600, 50);
        textLabel.setFont(customFont.deriveFont(24f));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        decoJPanel.add(textLabel);
        layeredPane.add(decoJPanel, Integer.valueOf(1));
    }

    private void game() {
        // 기본 탑로더 레이어
        JLabel toploaderLabel = new JLabel(new ImageIcon("assets/img/decoItem/toploader.png"));
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int xPosition = (frameWidth - 600) / 2;
        int yPosition = (frameHeight - 600) / 2;

        toploaderLabel.setBounds(xPosition, yPosition, 600, 600 + 180);
        layeredPane.add(toploaderLabel, Integer.valueOf(1));

        // 종료 버튼 설정
        ImageIcon endIcon = new ImageIcon("assets/img/decoItem/endBtn.png");
        JButton endButton = new JButton(endIcon);
        endButton.setSize(200, 100);
        endButton.setLocation((getWidth() - endButton.getWidth()) / 2, getHeight() - endButton.getHeight() - 60);
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
        layeredPane.add(endButton, Integer.valueOf(10));
        endButton.addActionListener(e -> {

            gameResult = gameEnd();
            
            dispose();
          
        });

    }

    private void updateLayers() {

        for (Component component : layeredPane.getComponents()) {
            if (layeredPane.getLayer(component) > 1 && layeredPane.getLayer(component) < 10) {
                layeredPane.remove(component);
            }
        }
        layeredPane.repaint();

        JLabel toploaderLabel = new JLabel(new ImageIcon("assets/img/decoItem/toploader.png"));
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int xPosition = (frameWidth - 600) / 2;
        int yPosition = (frameHeight - 600) / 2;

        toploaderLabel.setBounds(xPosition, yPosition, 600, 600 + 180);
        layeredPane.add(toploaderLabel, Integer.valueOf(1));

        // 심볼 레이어
        if (currentList[0] == 0) {
            JLabel heartLayer = new JLabel(new ImageIcon("assets/img/decoItem/heart.png"));
            heartLayer.setBounds(xPosition, yPosition, 600, frameHeight + 180);
            layeredPane.add(heartLayer, Integer.valueOf(2));
        } else if (currentList[0] == 1) {
            JLabel starLayer = new JLabel(new ImageIcon("assets/img/decoItem/star.png"));
            starLayer.setBounds(xPosition, yPosition, 600, frameHeight + 180);
            layeredPane.add(starLayer, Integer.valueOf(2));
        } else if (currentList[0] == 2) {
            JLabel auroraLayer = new JLabel(new ImageIcon("assets/img/decoItem/aurora.png"));
            auroraLayer.setBounds(xPosition, yPosition, 600, frameHeight + 180);
            layeredPane.add(auroraLayer, Integer.valueOf(2));
        }

        // 색상 레이어
        if (currentList[1] == 0) {
            JLabel pinkLayer = new JLabel(new ImageIcon("assets/img/decoItem/pink.png"));
            pinkLayer.setBounds(xPosition - 5, yPosition - 10, 600, frameHeight + 180);
            layeredPane.add(pinkLayer, Integer.valueOf(3));
        } else if (currentList[1] == 1) {
            JLabel blueLayer = new JLabel(new ImageIcon("assets/img/decoItem/blue.png"));
            blueLayer.setBounds(xPosition - 5, yPosition - 10, 600, frameHeight + 180);
            layeredPane.add(blueLayer, Integer.valueOf(3));
        } else if (currentList[1] == 2) {
            JLabel yellowLayer = new JLabel(new ImageIcon("assets/img/decoItem/yellow.png"));
            yellowLayer.setBounds(xPosition - 15, yPosition - 10, 600, frameHeight + 180);
            layeredPane.add(yellowLayer, Integer.valueOf(3));
        }

        // 파츠 레이어
        if (currentList[2] == 0) {
            JLabel pinkPartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsP.png"));
            pinkPartsLayer.setBounds(xPosition, yPosition, 580, frameHeight + 180);
            layeredPane.add(pinkPartsLayer, Integer.valueOf(4));
        } else if (currentList[2] == 1) {
            JLabel bluePartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsB.png"));
            bluePartsLayer.setBounds(xPosition, yPosition, 600, frameHeight + 180);
            layeredPane.add(bluePartsLayer, Integer.valueOf(4));
        } else if (currentList[2] == 2) {
            JLabel whitePartsLayer = new JLabel(new ImageIcon("assets/img/decoItem/partsW.png"));
            whitePartsLayer.setBounds(xPosition, yPosition, 600, frameHeight + 180);
            layeredPane.add(whitePartsLayer, Integer.valueOf(4));
        }

        layeredPane.repaint();
    }

    private void onCurrentListChanged(int index, int value) {

        updateCurrentList(index, value);
        updateLayers();

    }

    private void updateCurrentList(int index, int value) {
        if (currentList.length > index) {
            currentList[index] = value;

        }
        System.out.println("올바른 결과: " + java.util.Arrays.toString(selectedItems));
        System.out.println("Current List: " + java.util.Arrays.toString(currentList));
    }

    private void loadCustomFont() {
        customFont = FontManager.loadFont(10);
        UIManager.put("Label.font", customFont);
        UIManager.put("Button.font", customFont);
    }

    public void execute() {
        setVisible(true);
    }

    private boolean gameEnd() {
        if (currentList.length != selectedItems.length) {
            return false;
        }

        for (int i = 0; i < currentList.length; i++) {
            if (currentList[i] != selectedItems[i]) {
                return false;
            }
        }

        return true;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if (gameResult != null) {
            System.out.println(gameResult ? "맞았습니다" : "틀렸습니다");
        }
    }
    public static void main(String[] args) {
         new Deco();

    }
}