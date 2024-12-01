package Character;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class bcRequest extends Request {
    private static final String PATH = "assets/txt/bcLog.txt"; // 진상 대사 파일
    private final String bcLog; // 진상 대사

    protected Image request = new ImageIcon("assets/img/npc/bcQuest.png").getImage();

    private final bcAns ans;

    public bcRequest(Npc npc, bcAns ans) {
        super(npc);
        this.ans = ans;
        bcLog = setBcLog(); // 대사 설정
        customizeTimer();
    }

    // 대사 세팅
    public String setBcLog() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PATH), StandardCharsets.UTF_8))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            if (!lines.isEmpty()) {
                return lines.get(new java.util.Random().nextInt(lines.size()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "코스프레 무대 해도 되나요?";
    }

    // bc는 요청 생성 타이밍이 다름
    private void customizeTimer() {
        requestTimer = new Timer(2000, _ -> makeRequest());
        requestTimer.start();
    }

    @Override
    public void makeRequest() {
        if (!active) {
            active = true;
            requestTimer.stop();
        }
    }

    // 배경 추가해서 그리기
    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        if (active) {
            int balloonX = x + 40;
            int balloonY = y - 75;

            // bc용 말풍선
            g2d.drawImage(request, balloonX, balloonY, null);

            // 대사 출력
            g2d.setColor(Color.BLACK);
            g2d.drawString(bcLog, balloonX + 10, balloonY + 30);

            ans.draw(g2d, 360, 180);
        }
    }
}