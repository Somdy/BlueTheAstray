package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class WizardFieldPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("WizardFieldPower");
    
    public WizardFieldPower(AbstractCreature owner, int block) {
        super(ID, "channel", PowerType.BUFF, owner);
        setValues(block);
        preloadString(s -> setAmtValue(0, amount));
        updateDescription();
    }
    
    @Override
    public float onPlayerLosingBlock(float block) {
        if (amount > 0 && block > amount) {
            block -= amount;
        }
        return super.onPlayerLosingBlock(block);
    }
    
    @Override
    public void atStartOfTurnPostDraw() {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new WizardFieldPower(owner, amount);
    }
}