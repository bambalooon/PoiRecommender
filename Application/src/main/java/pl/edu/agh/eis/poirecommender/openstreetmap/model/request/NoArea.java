package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

public class NoArea implements Area {
    @Override
    public String createQueryPart() {
        return "";
    }
}
