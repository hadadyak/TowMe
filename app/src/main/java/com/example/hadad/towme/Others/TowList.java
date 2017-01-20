package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.Tow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadad on 1/20/2017.
 */

public class TowList {
    public static final List<Tow> ITEMS = new ArrayList<Tow>();

    private static void addItem(Tow item) {
        ITEMS.add(item);
    }

}
