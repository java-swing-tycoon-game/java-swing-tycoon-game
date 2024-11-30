package Items;

import Character.Move;

public class Tshirt {
    Move move;
    private int speedBoost = 10; // 티셔츠 착용 시 속도 증가

    public void tshirtEffect() {
        int currentSpeed = move.getMoveSpeed();    // 현재 캐릭터 이동 속도
        move.setMoveSpeed(currentSpeed + speedBoost);
        System.out.println("티셔츠 효과 적용.. 현재 이동 속도: " + move.getMoveSpeed());
    }
}
