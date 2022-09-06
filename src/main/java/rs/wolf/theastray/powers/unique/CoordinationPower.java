package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.utils.TAUtils;

public class CoordinationPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("CoordinationPower");
    
    public CoordinationPower(int amount) {
        super(ID, "static_discharge", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void onShuffle() {
        flash();
        addToBot(new ModifyManaAction(amount));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new CoordinationPower(amount);
    }
}