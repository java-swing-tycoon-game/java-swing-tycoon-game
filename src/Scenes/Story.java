package Scenes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Story extends JFrame {
    Story() {
        setTitle("Scenes.Story");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel storyLabel = new JLabel("스토리 설명");
        storyLabel.setBounds(50, 50, 700, 50);
        add(storyLabel);

        // 메인으로 돌아가기
        JButton backButton = new JButton("메인으로");
        backButton.setBounds(400, 200, 200, 50);

        // 메인으로 이동 버튼 클릭 시 이벤트
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
