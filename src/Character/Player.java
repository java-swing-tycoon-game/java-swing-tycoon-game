package Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Player extends Move {
    private Image characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();

    public Player(){
        this.characterImg = new ImageIcon("assets/img/playerCharacter.png").getImage();
        setOpaque(false);

        // 마우스 클릭 리스너 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // 각 원을 검사하여 클릭된 경우 해당 원의 목표 위치로 이동
                for (Place place : getPlaces()) {
                    if (place.contains(mouseX, mouseY)) {
                        moveToDest(place);
                        break;
                    }
                }
            }
        });
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

//        // Place들을 그려주는 예제
//        g2d.setColor(Color.RED);
//        g2d.setStroke(new BasicStroke(2));
//        for (Place place : getPlaces()) {
//            g2d.drawOval(place.x - place.radius, place.y - place.radius, place.radius * 2, place.radius * 2);
//        }
    }

}
