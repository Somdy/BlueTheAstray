package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.powers.MagicPower;

public class E107 extends AstrayExtCard {
    public E107() {
        super(107, 1, 7, CardTarget.SELF);
        setMagicValue(1, true);
        setExtraMagicValue(3, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new MagicPower(s, magicNumber)));
        atbTmpAction(() -> {
            for (int i = 0; i < getExtraMagic(); i++) {
                AbstractCard card = CardMst.ReturnRndCardInCombat(c -> true);
                addToTop(new MakeTempCardInHandAction(card, 1));
            }
        });
    }
    
    @Override
    public boolean canUpgrade() {
        return false;
    }
    
    @Override
    public void selfUpgrade() {}
}