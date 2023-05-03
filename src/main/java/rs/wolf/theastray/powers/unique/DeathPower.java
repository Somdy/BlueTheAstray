package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class DeathPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("DeathPower");
    
    public DeathPower(AbstractCreature owner, int immunity, int turns) {
        super(ID, "death", PowerType.BUFF, owner);
        setValues(immunity, turns);
        preloadString(s -> {
            setAmtValue(0, this.amount);
            setAmtValue(1, extraAmt);
        });
        updateDescription();
    }
    
    @Override
    public void preModifyDamage(DamageInfo info) {
        if (info.output < amount) {
            flashWithoutSound();
            info.output = 0;
            info.isModified = info.base != info.output;
        }
    }
    
    @Override
    public void atStartOfTurn() {
        if (extraAmt > 1) {
            extraAmt--;
        } else {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new DeathPower(owner, amount, extraAmt);
    }
}