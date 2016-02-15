package pl.edu.agh.eis.poirecommender.application.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NavigationDrawer {
    private static final String SAVED_CURRENT_FRAGMENT_INDEX = "SAVED_CURRENT_FRAGMENT_INDEX";
    private static final int STARTUP_ITEM = 0;

    private final DrawerLayout layout;
    private final ListView listView;
    private final List<NavigationDrawerItem> items;
    @Getter
    private NavigationDrawerItem currentItem;

    public void initCurrentItem() {
        currentItem = items.get(STARTUP_ITEM);
        listView.setItemChecked(STARTUP_ITEM, true);
    }

    public boolean isStartUpItem() {
        return currentItem == items.get(STARTUP_ITEM);
    }

    public boolean isDrawerOpen() {
        return layout.isDrawerOpen(listView);
    }

    public void closeDrawer() {
        layout.closeDrawer(listView);
    }

    public void saveState(Bundle state) {
        state.putInt(SAVED_CURRENT_FRAGMENT_INDEX, items.indexOf(currentItem));
    }

    public void restoreState(Bundle savedState) {
        int selectedItemIndex = savedState.getInt(SAVED_CURRENT_FRAGMENT_INDEX);
        currentItem = items.get(selectedItemIndex);
    }

    public void changeCurrentItem(int itemPosition) {
        currentItem = items.get(itemPosition);
        listView.setItemChecked(itemPosition, true);
    }
}
