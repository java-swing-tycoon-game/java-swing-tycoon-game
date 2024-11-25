package Character;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class bcRequest extends Request {
    private String bcLog; // 진상 대사
    private static final String PATH = "assets/txt/bcLog.txt"; // 진상 대사 파일
    protected Image request = new ImageIcon("assets/img/npc/bcQuest.png").getImage();

    private bcAns ans;

    public bcRequest(Npc npc) {
        super(npc, null);
        customizeTimer();
        bcLog = setBcLog(); // 대사 설정
    }

    private void customizeTimer() {
        // 기존 타이머를 멈추고 새 타이머 생성
        requestTimer = new Timer(2000, e -> {
            makeRequest(0, 0); // 좌표는 의미 없으므로 0, 0 전달
        });
        requestTimer.start(); // 새 타이머 시작
    }

    // null 오류 때문에 비워둠
    @Override
    protected void setZone(ArrayList<Place> places) { }

    //@Override
    public void makeRequest(int x, int y) {
        if (!active) {
            active = true;
            requestTimer.stop();
        }
    }

    public void setAns(bcAns ans) {
        this.ans = ans; // 외부에서 제공된 bcAns 설정
    }

    public bcAns getAns() {
        return ans; // bcAns 반환
    }

    // 대사 세팅
    public String setBcLog() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PATH), "UTF-8"))) {
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

        return "코스프레 무대 해도 되나요?"; // 파일 오류일 때 출력
    }


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

            if (ans != null && ans.ansActive) {
                ans.draw(g2d, 360, 180);
            }
        }
    }
}