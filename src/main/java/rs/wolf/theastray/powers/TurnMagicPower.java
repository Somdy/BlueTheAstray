package rs.wolf.theastray.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public final class TurnMagicPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("TurnMagicPower");
    
    public TurnMagicPower(AbstractCreature owner, int amount) {
        super(ID, "bias", PowerType.DEBUFF, owner);
        setValues(amount);
        preloadString(s -> setAmtValue(0, Math.abs(this.amount)));
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (owner.hasPower(MagicPower.ID)) {
            AbstractPower p = owner.getPower(MagicPower.ID);
            if (p != null) {
                flash();
                int loseAmt = Math.min(p.amount, amount);
                addToTop(new RemoveSpecificPowerAction(owner, owner, this));
                addToTop(new ReducePowerAction(owner, owner, p, loseAmt));
            }
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new TurnMagicPower(owner, amount);
    }
}