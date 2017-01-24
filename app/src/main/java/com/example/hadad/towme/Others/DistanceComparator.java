package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.Tow;

import java.util.Comparator;

/**
 * Created by Omer on 20-Jan-17.
 */

public class DistanceComparator implements Comparator<Tow> {
    public double x0,y0;

    public DistanceComparator(double x0,double y0){
        this.x0=x0;
        this.y0=y0;
    }

    public int compare(Tow o1, Tow o2) {
        double distanceVector1=(o1.getX()-x0)*(o1.getX()-x0)+(o1.getY()-y0)*(o1.getY()-y0);
        double distanceVector2=(o2.getX()-x0)*(o2.getX()-x0)+(o2.getY()-y0)*(o2.getY()-y0);
        if (distanceVector1 > distanceVector2)
            return 1;
        else if (distanceVector1< distanceVector2)
            return -1;
        else return 0;
    }
}
