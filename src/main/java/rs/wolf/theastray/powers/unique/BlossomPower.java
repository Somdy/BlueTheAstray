package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class BlossomPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("BlossomPower");
    private boolean energyRecharged;
    
    public BlossomPower(int drawAmt) {
        super(ID, "fasting", PowerType.BUFF, AbstractDungeon.player);
        setValues(drawAmt);
        preloadString(s -> setAmtValue(0, amount));
        updateDescription();
        energyRecharged = false;
    }
    
    @Override
    public void onInitialApplication() {
        energyRecharged = !AbstractDungeon.actionManager.turnHasEnded;
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        energyRecharged = false;
    }
    
    @Override
    public void onEnergyRecharge() {
        energyRecharged = true;
    }
    
    public void afterEnergyGained(int e) {
        if (energyRecharged && amount > 0 && e > 0 && !AbstractDungeon.actionManager.turnHasEnded) {
            flash();
            addToBot(new DrawCardAction(owner, amount * e));
        }
    }
    
    public void afterEnergyLost(int e) {
        if (energyRecharged && amount > 0 && e > 0 && !AbstractDungeon.actionManager.turnHasEnded) {
            flash();
            addToBot(new DrawCardAction(owner, amount * e));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new BlossomPower(amount);
    }
}