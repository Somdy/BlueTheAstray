package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class GiantPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("GiantPower");
    
    public GiantPower(AbstractCreature owner, int vuls, int blocks) {
        super(ID, "giant", PowerType.BUFF, owner);
        setValues(vuls, blocks);
        preloadString(s -> {
            setAmtValue(0, amount);
            setAmtValue(1, extraAmt);
        });
        updateDescription();
        isExtraAmtFixed = false;
    }
    
    @Override
    public void atStartOfTurn() {
        if (amount > 0) {
            addToBot(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, amount, !owner.isPlayer)));
        }
        if (extraAmt > 0) {
            addToBot(new GainBlockAction(owner, owner, extraAmt));
        }
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (amount > 0) {
            addToBot(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, amount, !owner.isPlayer)));
        }
        if (extraAmt > 0) {
            addToBot(new GainBlockAction(owner, owner, extraAmt));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new GiantPower(owner, amount, extraAmt);
    }
}
