package Character;

import GameManager.ClickManager;
import GameManager.NpcManager;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BlackConsumer extends Npc {
    // 타자게임 답변
    private bcAns ans;

    // 생성자
    public BlackConsumer() {
        super(player);

        // 타자 입력 받을 리스너
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                processKeyTyped(e.getKeyChar());
            }
        });

        // 키 이벤트를 받을 수 있도록 설정
        setFocusable(true);  // 키 이벤트를 받을 수 있게 설정
    }

    @Override
    public void setupRequest() {
        ans = new bcAns(); // 답장 설정
        request = new bcRequest(this, ans);
    }

    @Override // bc만 클릭 되도록
    public Rectangle setBounds() {
        clickBounds = new Rectangle(characterX, characterY, 1024, 800);
        return clickBounds;
    }

    @Override // 타자게임 시작
    public void onClick(Point clickPoint) {
        player.moveToCenter(() -> {
            if (request != null && !ans.ansActive) {
                ans.ansActive = true;

                requestFocusInWindow();
                repaint();
            }
        });
    }

    @Override
    public int getPriority() { return 3; }

    public void processKeyTyped(char keyChar) {
        if (ans.ansActive) {
            ans.processInput(keyChar);

            // 답장을 성공해야 타자게임 끝
            if (!ans.ansActive) {
                request.completeRequest(); // 요청 완료
                active = false;
                removeFromParent();

                repaint();
            }
        }
    }

    ////// 그리기 및 지우기 //////
    @Override
    protected void paintComponent(Graphics g) {
        //BlackConsumer 주위에 반투명 원 생성
        g.setColor(new Color(0, 0, 0, 128));
        g.fillOval(characterX - 100, characterY - 100, 200, 200);

        // 부모 클래스의 draw 호출
        super.paintComponent(g);
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();

        // 클릭 이벤트 추가 처리
        ClickManager.removeClickEventList(this);

        // NpcManager의 블랙 컨슈머 추가 처리
        if (this instanceof BlackConsumer) {
            NpcManager.setBcActive(false); // 블랙 컨슈머 플래그 초기화
        }
    }
}