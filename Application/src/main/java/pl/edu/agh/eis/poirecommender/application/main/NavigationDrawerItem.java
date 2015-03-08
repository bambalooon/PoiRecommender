package pl.edu.agh.eis.poirecommender.application.main;

/**
 * Created by BamBalooon on 2015-03-06.
 */
public class NavigationDrawerItem {
    private final String title;
    private final int iconResource;

    public NavigationDrawerItem(String title, int iconResource) {
        this.title = title;
        this.iconResource = iconResource;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResource() {
        return iconResource;
    }
}
