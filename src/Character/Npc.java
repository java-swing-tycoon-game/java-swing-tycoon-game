package Character;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Npc extends Move {

    // face와 leg는 공통 사용
    private final Image faceImg = new ImageIcon("assets/img/npc/face.png").getImage();
    private final Image[] legImg = {
            new ImageIcon("assets/img/npc/walking1.png").getImage(),
            new ImageIcon("assets/img/npc/walking2.png").getImage()
    };

    private Image eyeImg, hairImg, shirtsImg, pantsImg;

    // 이미지 경로
    private String[] eyeImgPath = {"assets/img/npc/eye1.png", "assets/img/npc/eye2.png"};
    private String[] hairImgPath = {"assets/img/npc/hair1.png", "assets/img/npc/hair2.png"};
    private String[] shirtsImgPath = {"assets/img/npc/shirts1.png", "assets/img/npc/shirts2.png"};
    private String[] pantsImgPath = {"assets/img/npc/pants1.png", "assets/img/npc/pants2.png"};

    private Image walkingImg;
    private int walkingIndex = 0;
    private BufferedImage buffer;

    // npc의 요청이 모두 처리되었는지 여부
    private boolean request = true;

    public Npc() {
        randomSetNpc();
        walkingAnimation();
    }

    // NPC 상태
    public void update() {

    }

    // 요청 생성
    private void makeRequest()
    {

    }

    ////////// NPC 이미지 관련 //////////

    // NPC 이미지 조합하기
    private void randomSetNpc()
    {
        eyeImg = setRandomImage(eyeImgPath);
        hairImg = setRandomImage(hairImgPath);
        shirtsImg = setRandomImage(shirtsImgPath);
        pantsImg = setRandomImage(pantsImgPath);
    }

    // 랜덤으로 이미지 설정
    private Image setRandomImage(String[] imageParts) {
        Random random = new Random();
        String path = imageParts[random.nextInt(imageParts.length)];
        return new ImageIcon(path).getImage();
    }

    // 걷기 애니메이션
    private void walkingAnimation() {
        Timer walkingTimer = new Timer(200, e -> {
            walkingIndex = (walkingIndex + 1) % legImg.length;
            walkingImg = legImg[walkingIndex];
            repaint();
        });
        walkingTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buffer == null) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 캐릭터 생성되는 위치
        int imageX = 500;
        int imageY = 300;

        g2d.drawImage(walkingImg, imageX, imageY, null);
        g2d.drawImage(pantsImg, imageX, imageY, null);
        g2d.drawImage(shirtsImg, imageX, imageY, null);
        g2d.drawImage(faceImg, imageX, imageY, null);
        g2d.drawImage(hairImg, imageX, imageY, null);
        g2d.drawImage(eyeImg, imageX, imageY, null);

        g2d.dispose();

        // 최종적으로 화면에 그리기
        g.drawImage(buffer, 0, 0, null);
    }
}
