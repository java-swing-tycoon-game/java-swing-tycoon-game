package Character;

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
        requestFocusInWindow();  // 포커스를 현재 컴포넌트로 설정
    }

    @Override
    // 요청을 만들러 간다. bc 때문에 분리해봄
    protected void moveToRequest() {
        moveToDest(new Place(512, 330, 0, 512, 330));
        setupRequest();
    }

    @Override
    protected void setupRequest() {
        // 요청 생성
        request = new bcRequest(characterX,characterY);
        ans = new bcAns("제발 나가주세요");
        ((bcRequest) request).setAns(ans);
    }

    @Override // 요청 완료 처리
    public void onClick(Point clickPoint) {
        if (request != null && !ans.ansActive) {
            System.out.println("bc 클릭됨");
            ans.ansActive = true;
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
                    removeFromParent();
                }
                repaint(); // 화면 갱신
            }
        }
    }


    protected void removeFromParent() {
        Container parent = getParent(); // 상위 컨테이너 참조
        if (parent != null) {
            parent.remove(this); // 상위 컨테이너에서 this 제거
            parent.revalidate(); // 레이아웃 재계산
            parent.repaint(); // 화면 갱신
        }
    }

}