package Scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Story extends JFrame {
    public Story() {
        setTitle("청춘 소녀는 콘서트의 꿈을 꾸지 않는다");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#D5F2FF"));

        JPanel imagePanel = new JPanel();
        int padding = 50;

        imagePanel.setBounds(padding, padding-10, 924, 640);
        imagePanel.setOpaque(false);

        ImageIcon howtoBg = new ImageIcon("assets/img/story.png");
        Image scaledImage = howtoBg.getImage().getScaledInstance(924, 620, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        imagePanel.add(imageLabel);
        add(imagePanel);

        // 버튼 설정
        JButton backButton = new JButton("메인으로");
        backButton.setBounds(412, 700, 200, 30); // 하단에 작게 배치
        backButton.setFont(new Font("Serif", Font.PLAIN, 14));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Main();
                dispose();
            }
        });
        add(backButton);

        setVisible(true);
    }
}