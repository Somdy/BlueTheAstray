package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class DeathPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("DeathPower");
    
    public DeathPower(AbstractCreature owner, int immunity) {
        super(ID, "phantasmal", PowerType.BUFF, owner);
        setValues(immunity);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (amount > damageAmount) {
            flashWithoutSound();
            damageAmount = 0;
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }
    
    @Override
    public void atStartOfTurn() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new DeathPower(owner, amount);
    }
}