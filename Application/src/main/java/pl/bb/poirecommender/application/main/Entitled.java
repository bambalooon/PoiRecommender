package pl.bb.poirecommender.application.main;

/**
 * Created by Krzysztof Balon on 2014-10-23.
 */
public interface Entitled {
    String TITLE = Entitled.class.getName()+".TITLE";

    /**
     * @return title resource
     */
    int getTitleResource();
}
