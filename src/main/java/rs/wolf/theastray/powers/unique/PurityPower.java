package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

public class PurityPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("PurityPower");
    
    public PurityPower(AbstractCreature owner, int stack, int block) {
        super(ID, "master_reality", PowerType.BUFF, owner);
        setValues(stack, block);
        preloadString(s -> {
            int totalBlock = amount * extraAmt;
            setAmtValue(0, totalBlock);
        });
        updateDescription();
        isExtraAmtFixed = false;
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (noOtherCardsPlayed() && amount > 0 && extraAmt > 0) {
            addToBot(new GainBlockAction(owner, amount * extraAmt));
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    boolean noOtherCardsPlayed() {
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .allMatch(c -> c.cardID.equals(GlobalIDMst.CardID("纯粹")));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new PurityPower(owner, amount, extraAmt);
    }
}