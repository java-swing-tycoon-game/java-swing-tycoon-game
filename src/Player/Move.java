package Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Move extends JPanel {
    // 캐릭터의 현재 좌표
    private int characterX = 512;
    private int characterY = 375;

    // 캐릭터가 이동할 목표 좌표
    private int targetX = 512;
    private int targetY = 375;

    // 이동 속도
    private final int MOVE_SPEED = 5;

    private Image characterImg;

    public Move() {
        characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
        // 패널을 투명하게 설정
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 클릭 위치를 목표 위치로 설정
                targetX = e.getX();
                targetY = e.getY();
            }
        });

        Timer timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCharacter();
                repaint(); // 화면 갱신
            }
        });
        timer.start();
    }

    private void moveCharacter() {
        // 현재 위치가 목표 위치와 다를 경우 이동
        if (characterX != targetX || characterY != targetY) {
            int dx = targetX - characterX;
            int dy = targetY - characterY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < MOVE_SPEED) {
                characterX = targetX;
                characterY = targetY;
            } else {
                characterX += (int) (MOVE_SPEED * dx / distance);
                characterY += (int) (MOVE_SPEED * dy / distance);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 캐릭터 이미지 그리기 (이미지의 중앙이 캐릭터 위치에 오도록 조정)
        int imageX = characterX - characterImg.getWidth(null) / 2;
        int imageY = characterY - characterImg.getHeight(null) / 2;
        g2d.drawImage(characterImg, imageX, imageY, null);
    }
}
