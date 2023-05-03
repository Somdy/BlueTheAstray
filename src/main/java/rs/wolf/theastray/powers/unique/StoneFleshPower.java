package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class StoneFleshPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("StoneFleshPower");
    private float percent;
    
    public StoneFleshPower(AbstractCreature owner, float percent) {
        super(ID, "stoneflesh", PowerType.BUFF, owner);
        this.percent = percent;
        setValues(-1);
        preloadString(s -> setAmtValue(0, SciPercent(this.percent)));
        updateDescription();
    }
    
    public void upgrade(float newBasePercent) {
        if (newBasePercent > percent) {
            percent = newBasePercent;
            flash();
            updateDescription();
        }
    }
    
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (percent > 0 && damage > 0) {
            damage -= damage * percent;
        }
        return super.atDamageFinalReceive(damage, type);
    }
    
    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new StoneFleshPower(owner, percent);
    }
}