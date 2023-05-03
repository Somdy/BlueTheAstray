package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class MagicalBodyPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MagicalBodyPower");
    
    public MagicalBodyPower(AbstractCreature owner, int block) {
        super(ID, "magicalbody", PowerType.BUFF, owner);
        setValues(block);
        preloadString(s -> setAmtValue(0, amount));
        updateDescription();
    }
    
    public void function(int excess) {
        if (excess > 0 && amount > 0) {
            flash();
            for (int i = 0; i < excess; i++) {
                addToBot(new GainBlockAction(cpr(), amount, true));
            }
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MagicalBodyPower(owner, amount);
    }
}