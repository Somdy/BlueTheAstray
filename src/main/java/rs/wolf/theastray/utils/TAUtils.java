package rs.wolf.theastray.utils;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.LMDebug;
import rs.lazymankits.utils.LMGameGeneralUtils;
import rs.wolf.theastray.core.Leader;

public interface TAUtils extends LMGameGeneralUtils {
    
    default String getSupLang() {
        return getSupportedLanguage(Settings.language);
    }
    
    @NotNull
    static String SupLang() {
        switch(Settings.language) {
            case ZHS:
                return "zhs";
            case ZHT:
                return "zht";
            default:
                return "eng";
        }
    }
    
    static void Log(Object what) {
        Leader.Log(what);
    }
    
    static void Log(Object who, Object what) {
        LMDebug.deLog(who, "THE ASTRAY-[LOG]> " + what);
    }
    
    @NotNull
    static String GetPrefix() {
        return Leader.PREFIX + ":";
    }
    
    @NotNull
    static String MakeID(String id) {
        return GetPrefix() + id;
    }
    
    @NotNull
    static String RmPrefix(@NotNull String idWithPrefix) {
        if (idWithPrefix.startsWith(GetPrefix()))
            return idWithPrefix.replaceFirst(GetPrefix(), "");
        return idWithPrefix;
    }
    
    static boolean RoomChecker(Class<? extends AbstractRoom> clz, AbstractRoom.RoomPhase phase) {
        return RoomAvailable() && clz.isInstance(AbstractDungeon.getCurrRoom()) && AbstractDungeon.getCurrRoom().phase == phase;
    }
    
    static boolean RoomChecker(Class<? extends AbstractRoom> clz) {
        return RoomAvailable() && clz.isInstance(AbstractDungeon.getCurrRoom());
    }
    
    static boolean RoomAvailable() {
        return AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null;
    }
    
    @NotNull
    static String CardImage(int localID) {
        return "AstrayAssets/images/cardports/" + localID + ".png";
    }
}