package pl.edu.agh.eis.poirecommender.pois.model;

import pl.edu.agh.eis.poirecommender.R;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public enum CardinalDirection {
    N(R.drawable.ic_arrow_up),
    E(R.drawable.ic_arrow_right),
    S(R.drawable.ic_arrow_down),
    W(R.drawable.ic_arrow_left),
    NE(R.drawable.ic_arrow_right_up),
    SE(R.drawable.ic_arrow_right_down),
    SW(R.drawable.ic_arrow_left_down),
    NW(R.drawable.ic_arrow_left_up);

    private final int iconResource;

    CardinalDirection(int iconResource) {
        this.iconResource = iconResource;
    }

    public int getIconResource() {
        return iconResource;
    }
}
