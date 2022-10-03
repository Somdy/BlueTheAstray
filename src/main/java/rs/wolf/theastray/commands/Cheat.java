package rs.wolf.theastray.commands;

public class Cheat {
    protected static final boolean[] CHEATS = new boolean[] {false, false, false, false, false};
    public static final int IEL = 0; // IgnoredEnlightenLimitation
    public static final int IMC = 1; // IgnoreManaCost
    public static final int SSD = 2; // ShowSecretDetails
    
    protected static void SetCheat(int index, boolean enabled) {
        if (index >= CHEATS.length || index < 0) return;
        CHEATS[index] = enabled;
    }
    
    public static boolean IsCheating(int index) {
        return index < CHEATS.length && CHEATS[index];
    }
}