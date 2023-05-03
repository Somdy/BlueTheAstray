package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.utils.TAUtils;

public class SequenceExhaustPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("SequenceExhaustPower");
    
    public SequenceExhaustPower(int cardsToGain) {
        super(ID, "sequence", PowerType.BUFF, AbstractDungeon.player);
        setValues(-1, cardsToGain);
        preloadString(s -> setAmtValue(0, extraAmt));
        updateDescription();
    }
    
    @Override
    public void onExhaust(AbstractCard card) {
        if (TAUtils.IsTrueMagical(card) && extraAmt > 0) {
            for (int i = 0; i < extraAmt; i++) {
                AbstractCard copy = CardMst.ReturnRndMagicInCombat(false);
                addToBot(new MakeTempCardInHandAction(copy, 1));
            }
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new SequenceExhaustPower(extraAmt);
    }
}