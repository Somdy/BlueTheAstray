package rs.wolf.theastray.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.characters.BlueTheAstray;

public class GlobalManaMst {
    public static void GainMana(int amt) {
        AbstractPlayer p = LMSK.Player();
        if (p instanceof BlueTheAstray)
            ((BlueTheAstray) p).manaMst.gainMana(amt);
    }
    
    public static void LoseMana(int amt) {
        AbstractPlayer p = LMSK.Player();
        if (p instanceof BlueTheAstray)
            ((BlueTheAstray) p).manaMst.useMana(amt);
    }
    
    public static void UseMana(int amt) {
        LoseMana(amt);
    }
    
    public static boolean HasEnoughMana(int amt) {
        AbstractPlayer p = LMSK.Player();
        if (p instanceof BlueTheAstray)
            return ((BlueTheAstray) p).manaMst.canUseMana(amt) && ((BlueTheAstray) p).manaMst.hasMana();
        return false;
    }
    
    public static boolean HasMana() {
        AbstractPlayer p = LMSK.Player();
        if (p instanceof BlueTheAstray)
            return ((BlueTheAstray) p).manaMst.hasMana();
        return false;
    }
}