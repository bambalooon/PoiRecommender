package pl.edu.agh.eis.poirecommender.application.main;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NavigationDrawerItem {
    private final String title;
    private final int iconResource;
    private final FragmentFactory fragmentFactory;
}
