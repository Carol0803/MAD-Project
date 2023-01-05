package w2.g16.odds.model;

import java.util.Comparator;

public class PriceComparator implements Comparator<Products> {
    @Override
    public int compare(Products o1, Products o2) {
        double p1 = o1.getPrice();
        double p2 = o2.getPrice();
        return Double.compare(p1,p2);
    }
}
