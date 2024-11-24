package Character;

import GameManager.ClickEvent;
import GameManager.ClickManager;
import GameManager.NpcManager;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.im.InputContext;

public class BlackConsumer extends Npc {
    private bcAns ans;

    public BlackConsumer() {
        super();

        // 키 입력을 처리할 이벤트 리스너 등록
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // 키 입력을 받아서 processKeyTyped 메소드 호출
                processKeyTyped(e.getKeyChar());
                System.out.println(e.getKeyChar());
            }
        });

        // 키 이벤트를 받을 수 있도록 설정
        setFocusable(true);  // 키 이벤트를 받을 수 있게 설정
    }

    @Override
    // 요청을 만들러 간다. bc 때문에 분리해봄
    protected void moveToRequest() {
        moveToDest(new Place(5, 512, 330, 0, 512, 330), false, null);
        setupRequest();
    }

    @Override
    public void setupRequest() {
        // 요청 생성
        request = new bcRequest(characterX,characterY);
        ans = new bcAns("제발 나가주세요");
        ((bcRequest) request).setAns(ans);
    }

    @Override
    public int getPriority() {
        return 3; // Player보다 높은 우선순위
    }

    @Override // 요청 완료 처리
    public void onClick(Point clickPoint) {
        if (request != null && !ans.ansActive) {
            System.out.println("bc 클릭됨");
            ans.ansActive = true;

            requestFocusInWindow();  // 포커스를 현재 컴포넌트로 설정
            repaint(); // 화면 갱신
        }
    }

    //BlackConsumer 전용 그래픽 효과 추가
    @Override
    protected void paintComponent(Graphics g) {
        // 부모 클래스의 draw 호출
        super.paintComponent(g);
    }

    public void processKeyTyped(char keyChar) {
        if (ans.ansActive) {
            ans.processInput(keyChar);

            // 성공/실패 여부 확인
            if (!ans.ansActive) {
                if (ans.isSuccess()) {
                    request.completeRequest(); // 요청 완료
                    active = false;
                    removeFromParent();
                }
                repaint(); // 화면 갱신
            }
        }
    }

    @Override
    public void removeFromParent() {
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this); // 이미지 제거
            parent.revalidate(); // 레이아웃 재계산
            parent.repaint(); // 화면 갱신

            // 클릭 이벤트 리스트에서 제거
            ClickManager.removeClickList(this);
            System.out.println("Removed from ClickManager: " + this);
            System.out.println(ClickManager.ClickEventList);

            // 포커스 이동 (다른 객체나 기본 컨테이너로)
            if (parent.getComponentCount() > 0) {
                parent.getComponent(0).requestFocusInWindow(); // 첫 번째 컴포넌트에 포커스 설정
            }

            // NpcManager와 상태 동기화
            if (this instanceof BlackConsumer) {
                NpcManager.setBcActive(false); // 블랙 컨슈머 플래그 초기화
            }
        }
    }

}