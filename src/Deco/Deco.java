package Deco;

import javax.swing.*;
import java.awt.*;
import GameManager.FontManager;

public class Deco extends JFrame {
    private Image backgroundImage;
    private JLabel comentJLabel;
    public static Font customFont;

    public Deco() {
        loadCustomFont();

        setTitle("탑로더를 꾸며보아요!");
        setSize(1038, 805);
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

        setContentPane(backgroundPanel);
        setLayout(null); 
        
        gameInfo(); 
        setVisible(true);
    }

    private void gameInfo() {
        comentJLabel = new JLabel("<html><center>탑로더를 손님이 원하는 테마에 맞춰 꾸며주세요! <br/> 손님이 원하지 않은 파츠를 올리면 추가 코인 ZERO!!</center></html>", SwingConstants.CENTER);
        comentJLabel.setOpaque(true); 
        comentJLabel.setBackground(new Color(255, 255, 255, 208)); 
        comentJLabel.setSize(950, 200); 
        comentJLabel.setLocation((getWidth() - comentJLabel.getWidth()) / 2, getHeight() - comentJLabel.getHeight() - 80); 

        comentJLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 5, true)); 
        comentJLabel.setHorizontalAlignment(SwingConstants.CENTER); 
        comentJLabel.setVerticalAlignment(SwingConstants.CENTER); 
        comentJLabel.setFont(customFont);
        Font largerFont = customFont.deriveFont(24f);  // 24px로 크기 변경
        comentJLabel.setFont(largerFont);
        add(comentJLabel);
    }

    private void loadCustomFont() {
        customFont = FontManager.loadFont();
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