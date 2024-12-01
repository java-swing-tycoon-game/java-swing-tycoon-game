package Scenes;

import GameManager.DayManager;
import GameManager.StartManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main extends JFrame {
    private JLabel bgImageLabel, imageLabel1, titleImageLabel;
    private JButton button1, button2, button3;

    public Main() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");
        setSize(1038, 805);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.decode("#e3f6ff")); // 전체 배경색 맞추기 (하늘색으로)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // 메인 배경
        bgImageLabel = new JLabel(new ImageIcon("assets/img/main/mainBackground.png"));

        // 메인 이미지
        imageLabel1 = new JLabel(new ImageIcon("assets/img/main/mainImg.png"));

        // 타이틀
        titleImageLabel = new JLabel(new ImageIcon("assets/img/title.png"));

        // 버튼
        button1 = new JButton(new ImageIcon("assets/img/main/howtoplayButton.png"));
        button1.setBorderPainted(false);
        button1.setContentAreaFilled(false);

        button2 = new JButton(new ImageIcon("assets/img/main/gamestartButton.png"));
        button2.setBorderPainted(false);
        button2.setContentAreaFilled(false);

        button3 = new JButton(new ImageIcon("assets/img/main/storyButton.png"));
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
                DayManager dayManager = DayManager.getInstance();
                if (dayManager.getDay() == 1) {new StartManager(dayManager, true);}
                else {new StartManager(dayManager, false);}
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
        titleImageLabel.setBounds((width - titleWidth) / 10 - 30, (int) (-50 * heightRatio), titleWidth, titleHeight);

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
