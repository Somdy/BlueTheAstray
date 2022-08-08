package rs.wolf.theastray.commands;

public class Cheat {
    protected static final boolean[] CHEATS = new boolean[] {false, false, false, false, false};
    public static final int IEL = 0; // IgnoredEnlightenLimitation
    
    public static boolean IsCheating(int index) {
        return index < CHEATS.length && CHEATS[index];
    }
}