package Character;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class bcAns {
    private static final String PATH = "assets/txt/bcAns.txt";

    private final String bcAns; // 목표 텍스트
    public static String userInput = ""; // 입력 받은 텍스트

    public boolean ansActive; // 타자 게임 활성화 여부

    private final Image request = new ImageIcon("assets/img/npc/bcAns.png").getImage();

    public bcAns() {
        this.bcAns = setBcAns();
        userInput = "";
        ansActive = false;
    }

    // 답장 세팅
    public String setBcAns() {
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

        return "되겠나요?";
    }

    // 입력이랑 비교 및 오류 알림
    public void processInput(char keyChar) {
        if (ansActive) {
            userInput += keyChar;

            if (bcAns.startsWith(userInput)) {
                if (userInput.equals(bcAns)) {
                    stop(); // 성공 시 게임 종료
                }
            } else {
                userInput = "입력오류. 한국어 확인";

                SwingUtilities.invokeLater(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    userInput = "";  // 입력 초기화
                });
            }
        }
    }

    public void stop() { ansActive = false; }

    public void draw(Graphics2D g2d, int x, int y) {
        if (ansActive) {
            // 말풍선
            g2d.drawImage(request, x, y, null);

            // 답변 텍스트 출력
            g2d.drawString("입력할 문장: " + bcAns, x + 10, y + 20);

            // 사용자 입력 텍스트 출력
            g2d.setColor(Color.red);
            g2d.drawString(userInput, x + 10, y + 40);
        }
    }
}
