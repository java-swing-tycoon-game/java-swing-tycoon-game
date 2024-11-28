package Character;

import java.util.ArrayList;

public class MovePlayer extends Move {
    private final ArrayList<Place> places;

    public MovePlayer(int x, int y) {
        super(x, y);
        this.places = new ArrayList<>();

        for (Place place : Move.places) {
            if (place.getNum() == 2)
            {
                this.places.add(new Place(
                        place.getNum(),
                        place.getX(),
                        place.getY(),
                        place.getRadius(),
                        place.getTargetX() + 100,
                        place.getTargetY() + 50
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

    @Override
    public ArrayList<Place> getPlaces() {
        return this.places;
    }
}
