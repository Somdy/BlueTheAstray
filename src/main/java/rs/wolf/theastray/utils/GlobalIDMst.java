package rs.wolf.theastray.utils;

import rs.wolf.theastray.data.DataMst;

public class GlobalIDMst {
    public static String CardID(int index) {
        return DataMst.ID(index);
    }
    
    public static String CardID(String localname) {
        return DataMst.ID(localname);
    }
}