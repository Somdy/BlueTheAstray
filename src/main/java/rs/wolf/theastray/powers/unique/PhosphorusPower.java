package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.utils.TAUtils;

public class PhosphorusPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("PhosphorusPower");
    
    public PhosphorusPower(int amount) {
        super(ID, "reactive", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void atStartOfTurnPostDraw() {
        if (amount > 0) {
            flash();
            addToBot(new QuickAction(() -> {
                AbstractCard draw = cpr().drawPile.getRandomCard(cardRandomRng());
                AbstractCard hand = cpr().hand.getRandomCard(cardRandomRng());
                AbstractCard discard = cpr().discardPile.getRandomCard(cardRandomRng());
                if (draw != null) {
                    addToTop(new ExhaustSpecificCardAction(draw, cpr().drawPile));
                }
                if (hand != null) {
                    addToTop(new ExhaustSpecificCardAction(hand, cpr().hand));
                }
                if (discard != null) {
                    addToTop(new ExhaustSpecificCardAction(discard, cpr().discardPile));
                }
                addToTop(new ApplyPowerAction(cpr(), cpr(), new MagicPower(cpr(), amount)));
            }));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new PhosphorusPower(amount);
    }
}