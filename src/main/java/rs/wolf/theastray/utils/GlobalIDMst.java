package rs.wolf.theastray.utils;

import rs.wolf.theastray.data.DataMst;

public class GlobalIDMst {
    public static String CardID(int index) {
        return DataMst.CardID(index);
    }
    
    public static String CardID(String localname) {
        return DataMst.CardID(localname);
    }
    
    public static String RelicID(int index) {
        return DataMst.RelicID(index);
    }
    
    public static String RelicID(String localname) {
        return DataMst.RelicID(localname);
    }
}