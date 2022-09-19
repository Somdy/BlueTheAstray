package rs.wolf.theastray.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.characters.BlueTheAstray;
import rs.wolf.theastray.patches.mechanics.ManaMechanicsPatch;
import rs.wolf.theastray.ui.manalayout.ManaMst;

public class GlobalManaMst {
    
    public static void InitPlayerMana(AbstractPlayer p) {
        if (p instanceof BlueTheAstray) {
            TAUtils.Log("[" + p.name + "] has own mana");
            return;
        }
        ManaMechanicsPatch.ManaField.manaMst.set(p, new ManaMst(p, 10, 0, ManaMst.Layout.LINE));
    }
    
    public static ManaMst GetPlayerManaMst(AbstractPlayer p) {
        if (p instanceof BlueTheAstray)
            return ((BlueTheAstray) p).manaMst;
        ManaMst mst = ManaMechanicsPatch.ManaField.manaMst.get(p);
        if (mst == null) {
            TAUtils.Log("[" + p.name + "] has no mana, initializing");
            InitPlayerMana(p);
            return GetPlayerManaMst(p);
        }
        return mst;
    }
    
    public static void ClearPostBattle() {
        AbstractPlayer p = LMSK.Player();
        if (p instanceof BlueTheAstray) {
            ((BlueTheAstray) p).manaMst.clearPostBattle();
            return;
        }
        ManaMst mst = ManaMechanicsPatch.ManaField.manaMst.get(p);
        if (mst != null) {
            mst.clearPostBattle();
            return;
        }
        TAUtils.Log("[" + p.name + "] has no mana, initializing");
        InitPlayerMana(p);
        ClearPostBattle();
    }
    
    public static void GainMana(int amt) {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        mst.gainMana(amt);
    }
    
    public static void LoseMana(int amt) {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        mst.useMana(amt);
    }
    
    public static void UseMana(int amt) {
        LoseMana(amt);
    }
    
    public static boolean HasEnoughMana(int amt) {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        return mst.canUseMana(amt) && mst.hasMana();
    }
    
    public static boolean HasMana() {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        return mst.hasMana();
    }
    
    public static boolean FullMana() {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        return mst.getCurrMana() >= mst.getMaxMana();
    }
    
    public static int CurrentMana() {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        return mst.getCurrMana();
    }
    
    public static int MaxMana() {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        return mst.getMaxMana();
    }
    
    public static int StartingMana() {
        AbstractPlayer p = LMSK.Player();
        ManaMst mst = GetPlayerManaMst(p);
        return mst.getStartingMana();
    }
}