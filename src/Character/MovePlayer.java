package Character;

import java.util.ArrayList;

public class MovePlayer extends Move {
    private final ArrayList<Place> places;

    public MovePlayer(int x, int y) {
        super(x, y);
        this.places = new ArrayList<>();

        for (Place place : Move.places) {
            if (place.getNum() == 2) // 룸
            {
                this.places.add(new Place(
                        place.getNum(),
                        place.getX(),
                        place.getY(),
                        place.getRadius(),
                        place.getTargetX() - 100,
                        place.getTargetY() + 50
                ));
            }
            if (place.getNum() == 3) // 대기 구역
            {
                this.places.add(new Place(
                        place.getNum(),
                        place.getX(),
                        place.getY(),
                        place.getRadius(),
                        place.getTargetX() + 50,
                        place.getTargetY() - 100
                ));
            }
            else{
                this.places.add(new Place(
                        place.getNum(),
                        place.getX(),
                        place.getY(),
                        place.getRadius(),
                        place.getTargetX(),
                        place.getTargetY()
                ));
            }
        }
    }

    public void setSpeed(int speed) {
        this.moveSpeed = speed;
    }

    @Override
    public ArrayList<Place> getPlaces() {
        return this.places;
    }
}
