package rs.wolf.theastray.powers.unique;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class DroughtPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("DroughtPower");
    
    public DroughtPower() {
        super(ID, "reactive", PowerType.DEBUFF, LMSK.Player());
        setValues(-1);
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new DroughtPower();
    }
    
    @SpirePatch2(clz = EnergyPanel.class, method = "addEnergy")
    public static class DroughtPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(int e) {
            if (LMSK.Player().hasPower(ID)) {
                AbstractPower p = LMSK.Player().getPower(ID);
                if (p != null) {
                    p.flash();
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }
}