package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.Tow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadad on 1/20/2017.
 */

public class TowList {
    public static final ArrayList<Tow> ITEMS = new ArrayList<Tow>();

    public static void addItem(Tow item) {
        ITEMS.add(item);
    }
    public static void hardCopy(ArrayList<Tow> tows){
        ITEMS.clear();
        ITEMS.addAll(tows);
    }
    public static int size(){
        return ITEMS.size();
    }
    public static Tow getTow(int pos){
        return ITEMS.get(pos);
    }

}
