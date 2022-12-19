package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.common.DrawExptCardAction;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

public class B39 extends AstrayExtCard {
    public B39() {
        super(39, 1, 6, CardTarget.NONE);
        setMagicValue(3, true);
//        setExtraMagicValue(1, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawExptCardAction(magicNumber, TAUtils::IsMagical, new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                for (AbstractCard card : DrawCardAction.drawnCards) {
                    if (card instanceof AstrayCard) {
                        ((AstrayCard) card).setMagicalDerivative(true);
                        String newDesc = card.rawDescription.replaceFirst("astray:魔法 ", "astray:衍生魔法 ");
                        ((AstrayCard) card).updateDescription(newDesc);
                    }
                }
            }
        }));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
    }
}