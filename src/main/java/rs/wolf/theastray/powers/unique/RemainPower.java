package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class RemainPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("RemainPower");
    
    public RemainPower(AbstractCreature owner, int blocks) {
        super(ID, "barricade", PowerType.BUFF, owner);
        setValues(blocks);
        preloadString(s -> setAmtValue(0, amount));
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        if (amount > 0) {
            flash();
            if (owner.isPlayer) {
                addToTop(new PressEndTurnButtonAction());
            }
            addToTop(new GainBlockAction(owner, amount));
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new RemainPower(owner, amount);
    }
}
