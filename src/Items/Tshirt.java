package Items;

import Character.MovePlayer;
import Scenes.Play;

import javax.swing.*;

public class Tshirt {
    private static final int SPEED_BOOST = 10;

    public static void applyEffect(MovePlayer player) {
        player.setSpeed(SPEED_BOOST);
        System.out.println("티셔츠 효과 적용 완료.");
        Play.player.imageChange("assets/img/playerCharacter_fast.png");
    }
}
