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
    NE(R.drawable.ic_arrow_up_right),
    SE(R.drawable.ic_arrow_down_right),
    SW(R.drawable.ic_arrow_down_left),
    NW(R.drawable.ic_arrow_up_left);

    private final int iconResource;

    CardinalDirection(int iconResource) {
        this.iconResource = iconResource;
    }

    public int getIconResource() {
        return iconResource;
    }
}
