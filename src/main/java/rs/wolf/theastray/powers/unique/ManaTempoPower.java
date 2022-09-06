package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class ManaTempoPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("ManaTempoPower");
    
    public ManaTempoPower(int perMana, int energyGain) {
        super(ID, "echo", PowerType.BUFF, AbstractDungeon.player);
        setValues(energyGain, perMana);
        preloadString(s -> {
            setAmtValue(0, extraAmt);
            setAmtValue(1, amount);
        });
        updateDescription();
    }
    
    @Override
    public void onManaLost(int lostAmt) {
        if (amount > 0 && lostAmt > 0) {
            flash();
            int gain = amount * lostAmt;
            addToBot(new GainEnergyAction(gain));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ManaTempoPower(extraAmt, amount);
    }
}