package Deco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Deco extends JFrame {
    private Image backgroundImage;
    private JLabel comentJLabel;
    private JButton nextButton;
    private JLayeredPane layeredPane;
    public static Font customFont;

    public Deco() {
        loadCustomFont();

        setTitle("탑로더를 꾸며보아요!");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundImage = new ImageIcon("assets/img/dacoBack.png").getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, 1000, 800);
        setContentPane(backgroundPanel);

        gameInfo();
        setVisible(true);
    }

    private void gameInfo() {
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(25, getHeight() - 280, 950, 200);
        layeredPane.setOpaque(false);
        layeredPane.setLayout(null);

        comentJLabel = new JLabel(
                "<html><center>탑로더를 손님이 원하는 테마에 맞춰 꾸며주세요! <br/> 손님이 원하지 않은 파츠를 올리면 추가 코인 ZERO!!</center></html>",
                SwingConstants.CENTER);
        comentJLabel.setOpaque(true);
        comentJLabel.setBackground(new Color(255, 255, 255));
        comentJLabel.setSize(950, 200);
        comentJLabel.setLocation(0, 0);
        comentJLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 5, true));
        comentJLabel.setHorizontalAlignment(SwingConstants.CENTER);
        comentJLabel.setVerticalAlignment(SwingConstants.CENTER);
        comentJLabel.setFont(customFont.deriveFont(24f));

        ImageIcon originalIcon = new ImageIcon("assets/img/allow.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(70, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        nextButton = new JButton(resizedIcon);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        nextButton.setSize(70, 50);
        nextButton.setLocation(850, 120);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] randomArray = randomArray();

                String part = "";
                if (randomArray[0] == 0) {
                    part = "<font color='#5A8BCE'>별</font>";
                } else if (randomArray[0] == 1) {
                    part = "<font color='#5A8BCE'>하트</font>";
                } else {
                    part = "<font color='#5A8BCE'>점박이</font>";
                }

                String backgroundColor = "";
                if (randomArray[1] == 0) {
                    backgroundColor = "<font color='#5A8BCE'>노란색</font>";
                } else if (randomArray[1] == 1) {
                    backgroundColor = "<font color='#5A8BCE'>파란색</font>";
                } else {
                    backgroundColor = "<font color='#5A8BCE'>분홍색</font>";
                }

                String concept = "";
                if (randomArray[2] == 0) {
                    concept = "<font color='#5A8BCE'>화이트</font>";
                } else if (randomArray[2] == 1) {
                    concept = "<font color='#5A8BCE'>블루</font>";
                } else {
                    concept = "<font color='#5A8BCE'>핑크</font>";
                }
                String text = String.format(
                        "난 %s 필름을 먼저 붙이고, %s 배경색으로 할래! <br/> 파츠는 %s 컨셉으로 하면 좋겠다!",
                        part, backgroundColor, concept);

                comentJLabel.setText("<html><center>" + text + "</center></html>");
            }
        });


        layeredPane.add(comentJLabel, Integer.valueOf(1));
        layeredPane.add(nextButton, Integer.valueOf(2));

        getContentPane().add(layeredPane);
    }

    public static int[] randomArray() {
        int[] array = new int[3]; 
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            array[i] = random.nextInt(3);
        }

        return array;
    }

    private void loadCustomFont() {
        try {
            File fontFile = new File("assets/font/ChangwonDangamAsac-Bold_0712.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            UIManager.put("Label.font", customFont);
            UIManager.put("Button.font", customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("폰트 로드 실패, 기본 폰트를 사용합니다.");
            customFont = new Font("Arial", Font.PLAIN, 18);
        }
    }

    public static void main(String[] args) {
        new Deco();
    }
}
