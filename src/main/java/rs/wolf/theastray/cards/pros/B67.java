package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

public class B67 extends AstrayProCard {
    public B67() {
        super(67, 1, CardTarget.NONE);
        exhaust = true;
        setStorage(true);
        addTags(TACardEnums.ILLUSION);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int count = 0;
            for (AbstractCard c : cpr().drawPile.group) {
                if (TAUtils.IsMagical(c)) {
                    count++;
                    addToTop(new ExhaustSpecificCardAction(c, cpr().drawPile));
                }
            }
            for (AbstractCard c : cpr().discardPile.group) {
                if (TAUtils.IsMagical(c)) {
                    count++;
                    addToTop(new ExhaustSpecificCardAction(c, cpr().discardPile));
                }
            }
            for (AbstractCard c : cpr().hand.group) {
                if (TAUtils.IsMagical(c)) {
                    count++;
                    addToTop(new ExhaustSpecificCardAction(c, cpr().hand));
                }
            }
            for (int i = 0; i < count; i++) {
                AbstractCard card = CardMst.ReturnRndCardInCombat(AstrayCard::isExtension, false);
                if (upgraded) card.upgrade();
                addToBot(new MakeTempCardInHandAction(card, 1));
            }
        });
    }
    
    AbstractCard getRndMagic() {
        AbstractCard card = CardMst.ReturnRndMagicInCombat(false, c -> true);
        if (upgraded) card.upgrade();
        card.applyPowers();
        return card;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}