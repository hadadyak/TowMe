package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.Tow;

import java.util.Comparator;

/**
 * Created by Omer on 20-Jan-17.
 */

public class RankeComparator implements Comparator<Tow> {
    @Override
    public int compare(Tow o1, Tow o2) {
        if (o1.getRank() > o2.getRank())
            return 1;
        else if (o1.getRank()< o2.getRank())
            return -1;
        else return 0;
    }
}
