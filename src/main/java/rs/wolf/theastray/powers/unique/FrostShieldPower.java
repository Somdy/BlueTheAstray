package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class FrostShieldPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("FrostShieldPower");
    
    public FrostShieldPower(AbstractCreature owner, int turns) {
        super(ID, "frostshield", PowerType.BUFF, owner);
        setValues(turns);
        preloadString(s -> s[0] = String.format(s[0], amount));
        updateDescription();
    }
    
    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new FrostShieldPower(owner, amount);
    }
}
