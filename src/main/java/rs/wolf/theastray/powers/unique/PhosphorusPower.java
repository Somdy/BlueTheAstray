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
                if (!cpr().drawPile.isEmpty()) {
                    AbstractCard draw = cpr().drawPile.getRandomCard(cardRandomRng());
                    addToTop(new ExhaustSpecificCardAction(draw, cpr().drawPile));
                }
                if (!cpr().hand.isEmpty()) {
                    AbstractCard hand = cpr().hand.getRandomCard(cardRandomRng());
                    addToTop(new ExhaustSpecificCardAction(hand, cpr().hand));
                }
                if (!cpr().discardPile.isEmpty()) {
                    AbstractCard discard = cpr().discardPile.getRandomCard(cardRandomRng());
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