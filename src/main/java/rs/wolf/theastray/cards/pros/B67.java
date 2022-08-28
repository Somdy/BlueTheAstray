package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;

public class B67 extends AstrayProCard {
    public B67() {
        super(67, 1, CardTarget.NONE);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            for (AbstractCard c : cpr().drawPile.group) {
                if (c.hasTag(TACardEnums.MAGICAL)) {
                    c.flash();
                    int index = cpr().drawPile.group.indexOf(c);
                    cpr().drawPile.group.set(index, getRndMagic());
                }
            }
            for (AbstractCard c : cpr().discardPile.group) {
                if (c.hasTag(TACardEnums.MAGICAL)) {
                    c.flash();
                    int index = cpr().discardPile.group.indexOf(c);
                    cpr().discardPile.group.set(index, getRndMagic());
                }
            }
            for (AbstractCard c : cpr().hand.group) {
                if (c.hasTag(TACardEnums.MAGICAL)) {
                    c.flash();
                    int index = cpr().hand.group.indexOf(c);
                    cpr().hand.group.set(index, getRndMagic());
                }
            }
        });
    }
    
    AbstractCard getRndMagic() {
        AbstractCard card = CardMst.ReturnRndMagicInCombat(c -> true);
        if (upgraded) card.upgrade();
        card.applyPowers();
        return card;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}