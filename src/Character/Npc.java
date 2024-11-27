package Character;

import GameManager.ClickEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Npc extends Move implements ClickEvent {
    ////// 이미지 경로 //////
    // face와 leg는 모든 npc가 공통 사용
    private final static Image faceImg = new ImageIcon("assets/img/npc/face.png").getImage();
    public final static Image[] legImg = {
            new ImageIcon("assets/img/npc/walking1.png").getImage(),
            new ImageIcon("assets/img/npc/walking2.png").getImage(),
            new ImageIcon("assets/img/npc/leg.png").getImage(),
    };
    private final static String[] eyeImgPath = {"assets/img/npc/eye1.png", "assets/img/npc/eye2.png"};
    private final static String[] hairImgPath = {"assets/img/npc/hair1.png", "assets/img/npc/hair2.png"};
    private final static String[] shirtsImgPath = {"assets/img/npc/shirts1.png", "assets/img/npc/shirts2.png"};
    private final static String[] pantsImgPath = {"assets/img/npc/pants1.png", "assets/img/npc/pants2.png"};

    private Image eyeImg, hairImg, shirtsImg, pantsImg;
    private Image walkingImg;
    private int walkingIndex = 0;

    private BufferedImage buffer;
    // 디버깅용
    protected Rectangle clickBounds;

    // 요청 클래스
    public Request request;
    private int requestCount = 0; // 요청 횟수
    private static int MAX_REQUESTS = 4; // 최대 요청 횟수
    protected boolean active; // npc 상태
    protected boolean isMoving = false; // 이동 중인지

    public static Player player;

    // 생성자
    public Npc(Player player) {
        super(960, 600); // 초기 좌표 설정
        Npc.player = player;

        randomSetNpc(); // npc 이미지 조합
        walkingAnimation();
        active = true;

        setupRequest();
    }

    public boolean getActive() { return active; }
    protected void finishNpc() { active = false; }

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
            if(isMoving)
            { walkingIndex = (walkingIndex + 1) % 2; }
            else { walkingIndex = 2; }

            walkingImg = legImg[walkingIndex];
            repaint();
        });
        walkingTimer.start();
    }

    ////// NPC 요청 //////
    // 요청 생성
    public void setupRequest() { request = new Request(this); }

    @Override // npc 범위만 클릭해서 요청 수행하도록
    public Rectangle setBounds() {
        int imageWidth = Npc.faceImg.getWidth(null);
        int imageHeight = Npc.faceImg.getHeight(null);
        clickBounds = new Rectangle(characterX-120/2, characterY-150/2, imageWidth, imageHeight);
        return clickBounds;
    }

    // 수정 필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override // 클릭 되면 요청 완료 처리
    public void onClick(Point clickPoint) {
        if (request != null && request.getActive()) {
            // Player를 NPC 위치로 이동
            player.moveToDest(new Place(0, characterX, characterY, 0, characterX, characterY), true, () -> {
                // Player 이동 완료 후 요청 비교
                if (request.getActive()) {
                    if (giveItem(player)) {
                        request.completeRequest(); // 요청 완료 처리
                        requestCount++;
                        System.out.println("NPC 요청 완료! 현재 요청 횟수: " + requestCount);

                        // 최대 요청 횟수 도달 시 NPC 비활성화
                        if (requestCount >= MAX_REQUESTS) {
                            active = false;
                            removeFromParent();
                            System.out.println("NPC가 비활성화되었습니다.");
                        }
                    } else {
                        //System.out.println("Player의 아이템이 요청과 일치하지 않습니다.");
                    }
                }else {
                    System.out.println("요청이 이미 완료되었습니다.");
                }

            });
        }else {
            System.out.println("NPC 요청이 활성화되지 않았거나 Player가 처리 중입니다.");
        }
    }

    @Override
    public int getPriority() { return 2; }

    // 플레이어가 가지고 있는 아이템과 요청을 비교
    protected boolean giveItem(Player player) {
        Image leftItem = player.getHoldItemL();
        Image rightItem = player.getHoldItemR();
        Image requestedItem = request.getRequestItem();

        // 요청 아이템 존재x
        if (requestedItem == null) {
            return false;
        }

        // 왼손과 요청 아이템 비교
        if (requestedItem.equals(leftItem)) {
            player.setHoldItemL(null); // 왼손 아이템 제거
            return true;
        }

        // 오른손과 요청 아이템 비교
        if (requestedItem.equals(rightItem)) {
            player.setHoldItemR(null); // 오른손 아이템 제거
            return true;
        }

        // 요청 아이템과 양 손 모두 불일치
        return false;
    }

    @Override
    public void moveToDest(Place place, boolean viaCenter, Runnable callback) {
        // 요청 완료 전에는 이동 불가, 위치 유지
        if (request != null && request.getActive()) { return; }
        super.moveToDest(place, viaCenter, callback);
    }

    @Override
    public void moveToTarget(Place place, Runnable callback) {
        if(isMoving) return;

        isMoving = true;
        super.moveToTarget(place, () -> {
            isMoving = false;
        });
    }

    ////// 그리기 및 지우기 //////
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buffer == null) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 캐릭터 이미지 그리기 (이미지의 중앙이 캐릭터 위치에 오도록 조정)
        int imageX = characterX - 60;
        int imageY = characterY - 75;

        g2d.drawImage(walkingImg, imageX, imageY, null);
        g2d.drawImage(pantsImg, imageX, imageY, null);
        g2d.drawImage(shirtsImg, imageX, imageY, null);
        g2d.drawImage(faceImg, imageX, imageY, null);
        g2d.drawImage(hairImg, imageX, imageY, null);
        g2d.drawImage(eyeImg, imageX, imageY, null);

        // 디버깅용
        g2d.setColor(Color.RED);
        g2d.drawRect(imageX, imageY, faceImg.getWidth(null), faceImg.getHeight(null));

        // 요청 있으면 요청 그리기
        if (request != null && request.getActive()) {
            request.draw(g2d, imageX, imageY);
        }

        g2d.dispose();

        // 최종적으로 화면에 그리기
        g.drawImage(buffer, 0, 0, null);
    }

    // Npc 화면에서 삭제
    public void removeFromParent() {
        Container parent = getParent();

        parent.remove(this); // 이미지 제거
        parent.revalidate(); // 레이아웃 재계산
        parent.repaint(); // 화면 갱신

        // 포커스 이동 (다른 객체나 기본 컨테이너로)
        if (parent.getComponentCount() > 0) {
            parent.getComponent(0).requestFocusInWindow(); // 첫 번째 컴포넌트에 포커스 설정
        }
    }
}
