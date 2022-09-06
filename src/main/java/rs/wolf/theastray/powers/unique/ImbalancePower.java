package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public final class ImbalancePower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("ImbalancePower");
    
    public ImbalancePower(int drawAmt, int discardAmt) {
        super(ID, "carddraw", PowerType.BUFF, AbstractDungeon.player);
        setValues(drawAmt, discardAmt);
        preloadString(s -> {
            setAmtValue(0, amount);
            setAmtValue(1, extraAmt);
        });
        updateDescription();
        AbstractDungeon.player.gameHandSize += drawAmt;
        stackable = false;
    }
    
    public void upgrade(int newDiscardAmt) {
        if (extraAmt != newDiscardAmt) {
            extraAmt = newDiscardAmt;
            updateDescription();
        }
    }
    
    @Override
    public void onRemove() {
        AbstractDungeon.player.gameHandSize -= amount;
    }
    
    @Override
    public void atStartOfTurnPostDraw() {
        if (extraAmt > 0) {
            flash();
            addToBot(new DiscardAction(owner, owner, extraAmt, true));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ImbalancePower(amount, extraAmt);
    }
}