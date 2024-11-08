import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    Main() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");
        setSize(1038, 805);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // 레이아웃 널로 설정 --> 위치 지정 마음대로

        // 메인 화면 이미지 + 타이틀
        ImageIcon bgImage = new ImageIcon("assets/img/mainBackground.png"); // 배경
        JLabel bgImageLabel = new JLabel(bgImage);
        bgImageLabel.setBounds(0, 0, bgImage.getIconWidth(), bgImage.getIconHeight());

        ImageIcon image1 = new ImageIcon("assets/img/main1.png"); // 이미지1
        JLabel imageLabel1 = new JLabel(image1);
        imageLabel1.setBounds(0, 0, image1.getIconWidth(), image1.getIconHeight());

        ImageIcon titleImage = new ImageIcon("assets/img/title.png"); // 타이틀
        JLabel titleImageLabel = new JLabel(titleImage);
        titleImageLabel.setBounds(-30, -70, titleImage.getIconWidth(), titleImage.getIconHeight());

        // 버튼 이미지
        ImageIcon buttonImage1 = new ImageIcon("assets/img/howtoplayButton.png");
        ImageIcon buttonImage2 = new ImageIcon("assets/img/gamestartButton.png");
        ImageIcon buttonImage3 = new ImageIcon("assets/img/storyButton.png");

        // 버튼 1 설정 (게임방법)
        JButton button1 = new JButton(buttonImage1);
        button1.setBounds(412, 455, buttonImage1.getIconWidth(), buttonImage1.getIconHeight());
        button1.setBorderPainted(false); // 버튼 테두리 제거
        button1.setContentAreaFilled(false); // 버튼 배경 제거

        // 버튼 2 설정 (게임시작)
        JButton button2 = new JButton(buttonImage2);
        button2.setBounds(412, 555, buttonImage2.getIconWidth(), buttonImage2.getIconHeight());
        button2.setBorderPainted(false);
        button2.setContentAreaFilled(false);

        // 버튼 3 설정 (스토리)
        JButton button3 = new JButton(buttonImage3);
        button3.setBounds(412, 655, buttonImage3.getIconWidth(), buttonImage3.getIconHeight());
        button3.setBorderPainted(false);
        button3.setContentAreaFilled(false);

        // 게임방법 버튼 클릭 시 이벤트
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Howto(); // Howto.java 창 열기
                dispose(); // 현재 창 닫기
            }
        });

        // 게임시작 버튼 클릭 시 이벤트
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 추후에 게임 화면 창 제작 시 추가될 코드는 여기에.............
                new Play();
                dispose(); // 현재 창 닫기
            }
        });

        // 스토리 버튼 클릭 시 이벤트
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Story(); // Story.java 창 열기
                dispose(); // 현재 창 닫기
            }
        });

        // 프레임에 이미지 추가
        add(titleImageLabel);
        add(button1);
        add(button2);
        add(button3);
        add(imageLabel1);
        add(bgImageLabel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
