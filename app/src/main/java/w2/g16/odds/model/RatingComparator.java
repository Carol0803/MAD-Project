package w2.g16.odds.model;

import java.util.Comparator;

public class RatingComparator implements Comparator<Products> {
    @Override
    public int compare(Products o1, Products o2) {
        double p1 = o1.getRating();
        double p2 = o2.getRating();
        return Double.compare(p1,p2);
    }
}
