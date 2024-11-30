package Items;

import Character.MovePlayer;

public class Tshirt {
    private static final int SPEED_BOOST = 15;

    public static void applyEffect(MovePlayer player) {
        player.setSpeed(SPEED_BOOST);
        System.out.println("티셔츠 효과 적용 완료.");
    }
}
