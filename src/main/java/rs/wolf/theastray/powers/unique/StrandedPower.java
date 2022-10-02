package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class StrandedPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("StrandedPower");
    
    public StrandedPower(AbstractCreature owner, int blocks) {
        super(ID, "barricade", PowerType.BUFF, owner);
        setValues(blocks);
        preloadString(s -> setAmtValue(0, amount));
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        if (amount > 0) {
            flash();
            addToTop(new GainBlockAction(owner, amount));
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new StrandedPower(owner, amount);
    }
}
