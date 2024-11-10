import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Npc extends JPanel {

    private final Image faceImg = new ImageIcon("assets/img/npc/face.png").getImage();
    private final Image legImg = new ImageIcon("assets/img/npc/leg.png").getImage();
    private Image eyeImg;
    private Image hairImg;
    private Image shirtsImg;
    private Image pantsImg;

    public Npc() {
        // 이미지 경로
        String[] eyeImages = {"assets/img/npc/eye1.png", "assets/img/npc/eye2.png"};
        String[] hairImages = {"assets/img/npc/hair1.png", "assets/img/npc/hair2.png"};
        String[] shirtsImages = {"assets/img/npc/shirts1.png", "assets/img/npc/shirts2.png"};
        String[] pantsImages = {"assets/img/npc/pants1.png", "assets/img/npc/pants2.png"};

        // 랜덤
        Random random = new Random();
        eyeImg = new ImageIcon(eyeImages[random.nextInt(eyeImages.length)]).getImage();
        hairImg = new ImageIcon(hairImages[random.nextInt(hairImages.length)]).getImage();
        shirtsImg = new ImageIcon(shirtsImages[random.nextInt(shirtsImages.length)]).getImage();
        pantsImg = new ImageIcon(pantsImages[random.nextInt(pantsImages.length)]).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 캐릭터 이미지 그리기
        int imageX = 500;
        int imageY = 300;
        g2d.drawImage(legImg, imageX, imageY, null);
        g2d.drawImage(pantsImg, imageX, imageY, null);
        g2d.drawImage(shirtsImg, imageX, imageY, null);
        g2d.drawImage(faceImg, imageX, imageY, null);
        g2d.drawImage(hairImg, imageX, imageY, null);
        g2d.drawImage(eyeImg, imageX, imageY, null);
    }
}
