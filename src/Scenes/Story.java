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
        setLayout(null);
        getContentPane().setBackground(Color.decode("#D5F2FF"));

        JPanel imagePanel = new JPanel();
        int padding = 50;

        imagePanel.setBounds(padding, padding-10, 924, 640);
        imagePanel.setOpaque(false);

        ImageIcon howtoBg = new ImageIcon("assets/img/howtoBack.png");
        Image scaledImage = howtoBg.getImage().getScaledInstance(924, 620, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        imagePanel.add(imageLabel);
        add(imagePanel);

        JLabel label = new JLabel("스토리 설명");
        label.setBounds(100, 100, 700, 50);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        add(label);


        JButton backButton = new JButton("메인으로");
        backButton.setBounds(400, 200, 200, 50);
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
