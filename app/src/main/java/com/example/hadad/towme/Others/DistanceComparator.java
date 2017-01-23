package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.Tow;

import java.util.Comparator;

/**
 * Created by Omer on 20-Jan-17.
 */

public class DistanceComparator implements Comparator<Tow> {
    public int compare(Tow o1, Tow o2) {
        double distanceVector1=Math.sqrt(o1.getX()*o1.getX()+o1.getY()*o1.getY());
        double distanceVector2=Math.sqrt(o2.getX()*o2.getX()+o2.getY()*o2.getY());
        if (distanceVector1 > distanceVector2)
            return 1;
        else if (distanceVector1< distanceVector2)
            return -1;
        else return 0;
    }
}
