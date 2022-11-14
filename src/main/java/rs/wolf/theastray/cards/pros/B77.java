package rs.wolf.theastray.cards.pros;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;

public class B77 extends AstrayProCard {
    public B77() {
        super(77, 0, CardTarget.NONE);
        setMagicValue(0, true);
        setMagicalDerivative(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int amount = BaseMod.MAX_HAND_SIZE - cpr().hand.size();
            for (int i = 0; i < amount; i++) {
                AbstractCard copy = CardMst.ReturnRndMagicInCombat(false);
                copy.setCostForTurn(copy.costForTurn - magicNumber);
                addToTop(new MakeTempCardInHandAction(copy, 1));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        exhaust = false;
    }
}
