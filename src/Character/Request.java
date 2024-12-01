package Character;

import GameManager.ItemManager;
import GameManager.NpcManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Request {
    protected Npc npc;

    private String requestItemPath; // 요청 아이템의 경로
    protected Image requestItemImage; // 요청 아이템의 이미지
    private final BufferedImage requestImg; // 요청 말풍선 이미지

    private final ArrayList<Place> zone; // 요청과 연관된 장소

    protected Timer requestTimer; // 요청 발생 타이머
    protected Timer failTimer; // 시간제한을 위한 타이머
    private final Timer progressTimer; // 요청 애니메이션을 위한 타이머
    private int progress = 0; // 진행도 (0~100)

    protected boolean active; // 개별 요청의 완료 여부
    //public boolean fail = false;

    public Request(Npc npc) {
        this.npc = npc;
        active = false;
        zone = new ArrayList<>();
        setZone();

        requestImg = loadImage();

        // 60초 실패 여부 판단
        failTimer = new Timer(60000, _ -> {
            failRequest(); // 요청 실패 처리
        });

        progressTimer = new Timer(1000, _ -> {
            progress = Math.min(progress + (100 / 60), 100);
            updateProgressImage();
        });

        requestTimer = new Timer(5000, _ -> makeRequest());
        requestTimer.start();
    }

    public boolean getActive() {
        return active;
    }

    // room 3곳 저장
    protected void setZone()
    {
        zone.clear();

        for (Place place : Move.places) {
            if (place.getNum() == 2) zone.add(place);
        }
    }

    public void makeRequest() {
        if (!active) {
            requestItemPath = setRequestItemPath(npc.characterX, npc.characterY);
            requestItemImage = ItemManager.getImageByPath(requestItemPath);

            // 요청 아이템 확인
            if (requestItemPath == null) {
                //System.err.println("makeRequest(): 요청 아이템 경로가 null입니다.");
                requestItemPath = "assets/img/item/popcorn.png";
            }

            do {
                requestItemPath = setRequestItemPath(npc.characterX, npc.characterY);
            } while (requestItemPath == null);
            requestItemImage = ItemManager.getImageByPath(requestItemPath);

            if (requestItemPath.equals(ItemManager.getItemPath(3))) npc.specialCoin += 2;
            if (requestItemPath.equals(ItemManager.getItemPath(4))) npc.specialCoin += 5;
            if (requestItemPath.equals(ItemManager.getItemPath(5))) npc.specialCoin += 7;

            active = true;
            progress = 0;

            progressTimer.stop();
            failTimer.stop();
            requestTimer.stop();

            progressTimer.start();
            failTimer.start();

            System.out.println("makeRequest(): 요청 활성화 완료, 아이템 경로: " + requestItemPath);
        }
    }

    public Image getRequestItemImage() {
        return requestItemImage;
    }

    private void updateProgressImage() {
        progress = Math.min(progress, 100); // progress 최대값 제한
        double progressRatio = progress / 100.0; // 0.0 ~ 1.0 비율 계산

        if (requestImg == null) {
            System.err.println("Request image is null.");
            return;
        }

        // 위에서부터 D24D82 색상으로 변환된 이미지 생성
        BufferedImage customTintImage = createCustomTintImage(requestImg, progressRatio);

        // 기존 이미지를 변환된 이미지로 교체
        Graphics2D g2d = requestImg.createGraphics();
        g2d.drawImage(customTintImage, 0, 0, null);
        g2d.dispose();
    }

    // 특정 색상으로 위에서부터 변환 (알파 채널 유지)
    private BufferedImage createCustomTintImage(BufferedImage original, double progress) {
        BufferedImage tinted = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // 목표 색상 D24D82 (RGB 값: 210, 77, 130)
        final int targetRed = 210;
        final int targetGreen = 77;
        final int targetBlue = 130;

        // 계산된 높이
        int tintedHeight = (int) (original.getHeight() * progress);

        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int rgba = original.getRGB(x, y);

                // 알파 채널 추출
                int alpha = (rgba >> 24) & 0xFF;

                // 위쪽 영역만 변환
                if (y < tintedHeight) {
                    // 기존 RGB 추출
                    int red = (rgba >> 16) & 0xFF;
                    int green = (rgba >> 8) & 0xFF;
                    int blue = rgba & 0xFF;

                    // 목표 색상으로 이동 (progress에 따라 기존 색상과 목표 색상의 비율 조정)
                    int newRed = (int) (red + (targetRed - red) * progress);
                    int newGreen = (int) (green + (targetGreen - green) * progress);
                    int newBlue = (int) (blue + (targetBlue - blue) * progress);

                    // 새로운 픽셀 값 생성
                    int newPixel = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                    tinted.setRGB(x, y, newPixel);
                } else {
                    // 변환되지 않은 아래쪽 영역은 원본 유지
                    tinted.setRGB(x, y, rgba);
                }
            }
        }
        return tinted;
    }

    // BufferedImage 로드
    private BufferedImage loadImage() {
        try {
            return ImageIO.read(new File("assets/img/npc/request.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void failRequest() {
        if (active) {
            System.out.println("요청이 실패로 처리되었습니다.");
            active = false;         // 요청 비활성화
            progressTimer.stop();   // 진행도 업데이트 중지
            failTimer.stop();       // 실패 타이머 중지

            npc.setActive(false);
            NpcManager.finishNpc(npc);

            npc.setupRequest();
        }
    }

    // 요청 완료
    public void completeRequest() {
        if (active) {
            active = false;
            progressTimer.stop();
            failTimer.stop();
            requestTimer.start();
        }
    }

    public Image getRequestItem() {
        return requestImg;
    }

    public String getRequestItemPath() {
        return requestItemPath;
    }

    // visible 상태인 아이템 중에서 랜덤 선택
    private String getRandomVisibleItemPath(int startIndex, int endIndex) {
        List<String> visiblePaths = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            if (ItemManager.getVisible(i)) {
                visiblePaths.add(ItemManager.getItemPath(i));
            }
        }
        return visiblePaths.isEmpty() ? null : visiblePaths.get(new Random().nextInt(visiblePaths.size()));
    }

    // 장소에 맞춰 요청할 아이템 이미지 세팅
    private String setRequestItemPath(int x, int y) {
        if (zone.get(0).contains(x, y)) {
            return setGoodsRequest();
        } else if (zone.get(1).contains(x, y)) {
            return setDecoRequest();
        } else if (zone.get(2).contains(x, y)) {
            return setMovieRequest();
        }
        return setWaitingRequest();
    }

    // 대기 중 요청을 랜덤으로 선택
    private String setWaitingRequest() {
        return getRandomVisibleItemPath(0, 2);
    }

    // 무비 중 요청을 랜덤으로 선택
    private String setMovieRequest() {
        return getRandomVisibleItemPath(2, 4);
    }

    // 굿즈 중 요청을 랜덤으로 선택
    private String setGoodsRequest() {
        return getRandomVisibleItemPath(4, 6);
    }

    // 데코 요청
    private String setDecoRequest() {
        return "assets/img/item/deco.png";
    }

    public void draw(Graphics2D g2d, int x, int y) {
        if (active) {
            int balloonX = x - 70;
            int balloonY = y - 65;

            // 말풍선 이미지 그리기
            g2d.drawImage(requestImg, balloonX, balloonY, null);

            // 요청 아이템 이미지 표시
            g2d.drawImage(requestItemImage, balloonX, balloonY, null);
        }
    }
}