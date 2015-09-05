package pl.edu.agh.eis.poirecommender.openstreetmap.search;

class SearchCriteriaJoiner {
    private static final char GROUP_START = '[';
    private static final char GROUP_END = ']';

    public String join(Iterable<String> searchCriteria) {
        StringBuilder searchCriteriaBuilder = new StringBuilder();
        for (String searchCriteriaUnit : searchCriteria) {
            if (searchCriteriaUnit.length() <= 1) {
                searchCriteriaBuilder.append(searchCriteriaUnit);
            } else {
                searchCriteriaBuilder
                        .append(GROUP_START)
                        .append(searchCriteriaUnit)
                        .append(GROUP_END);
            }
        }
        return searchCriteriaBuilder.toString();
    }
}
