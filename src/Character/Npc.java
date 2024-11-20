package Character;

import GameManager.ClickEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Npc extends Move implements ClickEvent {
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

    // 요청 클래스
    private Request request;
    private Rectangle clickBounds;

    public Npc() {
        super(510, 520); // 초기 좌표 설정
        randomSetNpc();
        walkingAnimation();
        setupRequest(); // 디버깅용으로 요청 하나 생성
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

    ////// NPC 요청 //////
    private void setupRequest() {
        request = new Request();
    }

    @Override
    public Rectangle setBounds() {
        int imageWidth = faceImg.getWidth(null);
        int imageHeight = faceImg.getHeight(null);
        clickBounds = new Rectangle(characterX, characterY, imageWidth, imageHeight);
        return clickBounds;
    }

    @Override
    public void onClick(Point clickPoint) {
        if (request != null) {
            request.completeRequest(); // 클릭 시 요청 완료
            repaint();
        }
    }

    ////// 그리기 //////
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buffer == null) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(walkingImg, characterX, characterY, null);
        g2d.drawImage(pantsImg, characterX, characterY, null);
        g2d.drawImage(shirtsImg, characterX, characterY, null);
        g2d.drawImage(faceImg, characterX, characterY, null);
        g2d.drawImage(hairImg, characterX, characterY, null);
        g2d.drawImage(eyeImg, characterX, characterY, null);

        // 요청 있으면 그리기
        if (request != null && request.isActive()) {
            request.draw(g2d, characterX, characterY);
        }

        g2d.dispose();

        // 최종적으로 화면에 그리기
        g.drawImage(buffer, 0, 0, null);
    }
}
