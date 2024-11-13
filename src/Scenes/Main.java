package Scenes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.*;

public class Main extends JFrame {
    private JLabel bgImageLabel, imageLabel1, titleImageLabel;
    private JButton button1, button2, button3;
    public static Font customFont;

    Main() {

        loadCustomFont();

        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");
        setSize(1038, 805);
        getContentPane().setBackground(Color.decode("#e3f6ff")); // 전체 배경색 맞추기 (하늘색으로)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // 메인 배경
        bgImageLabel = new JLabel(new ImageIcon("assets/img/mainBackground.png"));

        // 메인 이미지
        imageLabel1 = new JLabel(new ImageIcon("assets/img/main1.png"));

        // 타이틀
        titleImageLabel = new JLabel(new ImageIcon("assets/img/title.png"));

        // 버튼
        button1 = new JButton(new ImageIcon("assets/img/howtoplayButton.png"));
        button1.setBorderPainted(false);
        button1.setContentAreaFilled(false);

        button2 = new JButton(new ImageIcon("assets/img/gamestartButton.png"));
        button2.setBorderPainted(false);
        button2.setContentAreaFilled(false);

        button3 = new JButton(new ImageIcon("assets/img/storyButton.png"));
        button3.setBorderPainted(false);
        button3.setContentAreaFilled(false);

        // 게임방법 버튼 클릭 시 이벤트
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Howto(); // Scenes.Howto.java 창 열기
                dispose(); // 현재 창 닫기
            }
        });

        // 게임시작 버튼 클릭 시 이벤트
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Play();
                dispose(); // 현재 창 닫기
            }
        });

        // 스토리 버튼 클릭 시 이벤트
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Story(); // Scenes.Story.java 창 열기
                dispose(); // 현재 창 닫기
            }
        });

        if (customFont != null) {
            titleImageLabel.setFont(customFont);
            button1.setFont(customFont);
            button2.setFont(customFont);
            button3.setFont(customFont);
        }
        add(titleImageLabel);
        add(button1);
        add(button2);
        add(button3);
        add(imageLabel1);
        add(bgImageLabel);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                componentPositions();   // 각 컴포넌트 크기랑 위치
            }
        });

        setVisible(true);
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

    private void componentPositions() {
        int width = getWidth();
        int height = getHeight();

        // 비율로 따지기
        double widthRatio = width / 1038.0;
        double heightRatio = height / 805.0;

        // 배경 이미지 위치랑 크기
        bgImageLabel.setBounds(0, 0, width, height);
        imageLabel1.setBounds(0, 0, width, height);

        // 타이틀 위치랑 크기
        int titleWidth = (int) (titleImageLabel.getIcon().getIconWidth() * widthRatio);
        int titleHeight = (int) (titleImageLabel.getIcon().getIconHeight() * heightRatio);
        titleImageLabel.setBounds((width - titleWidth) / 10, (int) (-50 * heightRatio), titleWidth, titleHeight);

        // 버튼 위치랑 크기 (어차피 버튼 3개 크기 다 같음)
        int buttonWidth = (int) (button1.getIcon().getIconWidth() * widthRatio);
        int buttonHeight = (int) (button1.getIcon().getIconHeight() * heightRatio);

        button1.setBounds((width - buttonWidth) / 2, (int) (455 * heightRatio), buttonWidth, buttonHeight);
        button2.setBounds((width - buttonWidth) / 2, (int) (555 * heightRatio), buttonWidth, buttonHeight);
        button3.setBounds((width - buttonWidth) / 2, (int) (655 * heightRatio), buttonWidth, buttonHeight);
    }

    public static void main(String[] args) {
        new Main();
    }
}
