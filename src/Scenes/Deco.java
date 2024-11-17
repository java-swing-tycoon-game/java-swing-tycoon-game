package Scenes;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GameManager.FontManager;

public class Deco extends JFrame {
    private JButton nextButton;
    public static Font customFont;
    private int clickCount = 0;
    private JLayeredPane layeredPane;
    private ArrayList<String> selectedItems = new ArrayList<>();

    public Deco() {
        loadCustomFont();

        setTitle("탑로더를 꾸며보아요!");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon originalIcon = new ImageIcon("assets/img/decoItem/Frame2.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        Image backgroundImage = scaledImage;

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
                330);
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
                    themaLayer();
                }
            }
        });

        commentPanel.add(nextButton);

        layeredPane.add(commentPanel, Integer.valueOf(2));

    }

    private String randomDecoItem() {
        Random random = new Random();

        String[] films = { "사랑하는 마음이 담긴", "내 스타를 위한 ", "하늘에 수놓은 비단같은 " };
        String selectedFilm = films[random.nextInt(films.length)];

        String[] colors = { "벚꽃", "푸른", "레몬" };
        String selectedColor = colors[random.nextInt(colors.length)];

        String[] themes = { "공주", "바다", "진주" };
        String selectedTheme = themes[random.nextInt(themes.length)];

        selectedItems.add(selectedFilm);
        selectedItems.add(selectedColor);
        selectedItems.add(selectedTheme);

        return selectedFilm + "</font> 필름을 씌우고<br/> <font color='#2DA5DC'>" + selectedColor
                + "</font> 색을 바른 다음<font color='#2DA5DC'> " + selectedTheme + "</font> 컨셉으로 결정!";
    }

    private void filmLayer() {
        JPanel filmJPanel = new JPanel(null);
        filmJPanel.setSize(200, 400);
        filmJPanel.setLocation(50, 30);
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
                starButton.setSize(150, 220);
                starButton.setLocation(30, 0);
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
                heartButton.setSize(150, 220);
                heartButton.setLocation(15, 80);
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
                auroraButton.setSize(150, 220);
                auroraButton.setLocation(0, 160);
            }
        });

        filmJPanel.add(auroraButton);
        filmJPanel.add(heartButton);
        filmJPanel.add(starButton);

        layeredPane.add(filmJPanel, Integer.valueOf(1));
    }

    private void colorLayer() {
        JPanel colorJPanel = new JPanel(null);
        colorJPanel.setSize(250, 250);
        colorJPanel.setLocation((getWidth() - colorJPanel.getWidth()) / 2, 00);
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
                blueButton.setSize(150, 170);

            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                blueButton.setSize(150, 150);

            }
        });

        yellowButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                yellowButton.setSize(150, 170);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                yellowButton.setSize(150, 150);
            }
        });

        pinkButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pinkButton.setSize(150, 170);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pinkButton.setSize(150, 150);
            }
        });

        colorJPanel.add(pinkButton);
        colorJPanel.add(yellowButton);
        colorJPanel.add(blueButton);

        layeredPane.add(colorJPanel, Integer.valueOf(1));
    }

    private void themaLayer() {
        JPanel themaJPanel = new JPanel(null);
        themaJPanel.setSize(180, 400);
        themaJPanel.setLocation(getWidth() - themaJPanel.getWidth() - 50, 30);

        themaJPanel.setBackground(new Color(1, 1, 1));

        layeredPane.add(themaJPanel, Integer.valueOf(1));
    }

    private void decoItem() {
        JPanel decoJPanel = new JPanel(null);
        decoJPanel.setSize(600, 50);
        decoJPanel.setLocation(50, getHeight() - decoJPanel.getHeight() - 60);
        decoJPanel.setOpaque(false);

        JLabel textLabel = new JLabel("\" " +
                selectedItems.get(0) + "\" " + " \" " + selectedItems.get(1) + "\" " + " \" " + selectedItems.get(2)
                + "\" ", SwingConstants.CENTER);
        textLabel.setOpaque(false);
        textLabel.setSize(600, 50);
        textLabel.setLocation(0, 0);
        textLabel.setFont(customFont.deriveFont(24f));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);
        textLabel.setForeground(new Color(217, 89, 217));

        decoJPanel.add(textLabel);
        layeredPane.add(decoJPanel, Integer.valueOf(1));
    }

    private void loadCustomFont() {
        customFont = FontManager.loadFont(10);
        UIManager.put("Label.font", customFont);
        UIManager.put("Button.font", customFont);
    }

    public void execute() {
        setVisible(true);
    }

    public static void main(String[] args) {
        new Deco();
    }
}
