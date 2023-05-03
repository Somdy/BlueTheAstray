package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.powers.BurntPower;
import rs.wolf.theastray.utils.TAUtils;

public class AshPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("AshPower");
    
    public AshPower(int exhausts) {
        super(ID, "ashes", PowerType.BUFF, AbstractDungeon.player);
        setValues(0, exhausts);
        preloadString(s -> {
            s[0] = DESCRIPTIONS[amount < extraAmt ? 0 : 1];
            int left = extraAmt - amount;
            setAmtValue(0, left);
        });
        updateDescription();
        isExtraAmtFixed = false;
    }
    
    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BurntPower && amount < extraAmt && source == owner && power.amount > 0) {
            addToBot(new GainBlockAction(owner, owner, power.amount));
        }
    }
    
    @Override
    public void onExhaust(AbstractCard card) {
        flash();
        amount++;
        if (amount >= extraAmt) {
            updateDescription();
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new AshPower(amount);
    }
}