package Items;

import Character.Move;

public class Tshirt {
    private int speedBoost = 10; // 티셔츠 착용 시 속도 증가

    public static void applyEffect() {
        //player.fastMove();
        System.out.println("티셔츠 효과 적용 완료.");
    }

//    public void tshirtEffect(Move characterMove) {
//        int currentSpeed = characterMove.getMoveSpeed();    // 현재 캐릭터 이동 속도
//        characterMove.setMoveSpeed(currentSpeed + speedBoost);
//        System.out.println("티셔츠 효과 적용.. 현재 이동 속도: " + characterMove.getMoveSpeed());
//    }
}
