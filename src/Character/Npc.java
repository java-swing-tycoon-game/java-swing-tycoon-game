package Character;

import GameManager.ClickEvent;
import GameManager.ClickManager;

import javax.swing.*;
import java.awt.*;
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
    private final String[] eyeImgPath = {"assets/img/npc/eye1.png", "assets/img/npc/eye2.png"};
    private final String[] hairImgPath = {"assets/img/npc/hair1.png", "assets/img/npc/hair2.png"};
    private final String[] shirtsImgPath = {"assets/img/npc/shirts1.png", "assets/img/npc/shirts2.png"};
    private final String[] pantsImgPath = {"assets/img/npc/pants1.png", "assets/img/npc/pants2.png"};

    private Image walkingImg;
    private int walkingIndex = 0;
    private BufferedImage buffer;
    protected Rectangle clickBounds;

    // 요청 클래스
    protected Request request;
    protected boolean active; // npc 상태

    // 생성자
    public Npc() {
        super(960, 600); // 초기 좌표 설정
        randomSetNpc(); // npc 이미지 조합
        walkingAnimation();
        active = true;

        moveToRequest();
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
    protected void setupRequest() {
        // 요청 생성
        request = new Request(characterX, characterY, places);
    }

    // 요청을 만들러 간다. bc 때문에 분리해봄
    protected void moveToRequest() {
        //moveToWait(() -> {
            setupRequest(); // 요청 생성
            // 타이머로 작동해서 좌표가 갱신이 안됐는데 실행되는 오류 때문에 수정함
        //}); // 대기존으로 이동
    }

    @Override // npc 범위만 클릭해서 요청 수행하도록
    public Rectangle setBounds() {
        int imageWidth = faceImg.getWidth(null);
        int imageHeight = faceImg.getHeight(null);
        clickBounds = new Rectangle(characterX-120/2, characterY-150/2, imageWidth, imageHeight);
        return clickBounds;
    }

    @Override
    public int getPriority() {
        return 2; // Player보다 높은 우선순위
    }

    @Override // 요청 완료 처리
    public void onClick(Point clickPoint) {
        if (request != null) {
            request.completeRequest(); // 클릭 시 요청 완료
            repaint();
        }
    }

    protected void finishNpc() {
        active = false;
    }

    public boolean getActive()
    {
        return active;
    }

    public void removeFromParent() {}

    ////// 그리기 //////
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buffer == null) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 캐릭터 이미지 그리기 (이미지의 중앙이 캐릭터 위치에 오도록 조정)
        int imageX = characterX - 120/ 2;
        int imageY = characterY - 150/ 2;

        g2d.drawImage(walkingImg, imageX, imageY, null);
        g2d.drawImage(pantsImg, imageX, imageY, null);
        g2d.drawImage(shirtsImg, imageX, imageY, null);
        g2d.drawImage(faceImg, imageX, imageY, null);
        g2d.drawImage(hairImg, imageX, imageY, null);
        g2d.drawImage(eyeImg, imageX, imageY, null);

        g2d.setColor(Color.RED); // 클릭 영역 표시용
        g2d.drawRect(imageX, imageY, faceImg.getWidth(null), faceImg.getHeight(null));
        g2d.setColor(Color.GREEN);
        g2d.fillOval(characterX, characterY, 10, 10);
        // 요청 있으면 그리기
        if (request != null && request.isActive()) {
            request.draw(g2d, imageX, imageY);
        }

        g2d.dispose();

        // 최종적으로 화면에 그리기
        g.drawImage(buffer, 0, 0, null);
    }
}
