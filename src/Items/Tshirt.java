package Items;

import Character.MovePlayer;

public class Tshirt {
    private static final int SPEED_BOOST = 10;

    public static void applyEffect(MovePlayer player) {
        //player.fastMove();
        System.out.println("티셔츠 효과 적용 완료.");
    }

//    public void tshirtEffect(Move characterMove) {
//        int currentSpeed = characterMove.getMoveSpeed();    // 현재 캐릭터 이동 속도
//        characterMove.setMoveSpeed(currentSpeed + speedBoost);
//        System.out.println("티셔츠 효과 적용.. 현재 이동 속도: " + characterMove.getMoveSpeed());
//    }
}
