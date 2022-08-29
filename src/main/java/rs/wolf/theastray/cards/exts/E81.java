package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.powers.unique.AbyssPower;

public class E81 extends AstrayExtCard {
    public E81() {
        super(81, 2, 14, CardTarget.ALL_ENEMY);
        setMagicValue(1, true);
        setExtraMagicValue(1, true);
        addTags(TACardEnums.ILLUSION);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new AbyssPower(magicNumber)));
        atbTmpAction(() -> {
            for (int i = 0; i < getExtraMagic(); i++) {
                AbstractCard card = CardLibrary.getCurse().makeCopy();
                addToTop(new MakeTempCardInDiscardAction(card, 1));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(1);
    }
}