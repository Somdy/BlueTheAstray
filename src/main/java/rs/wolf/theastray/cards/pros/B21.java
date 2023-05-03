package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.tools.GridCardManipulator;
import rs.lazymankits.actions.utility.SimpleGridCardSelectBuilder;
import rs.wolf.theastray.cards.AstrayProCard;

public class B21 extends AstrayProCard {
    public B21() {
        super(21, 1, CardTarget.NONE);
        setMagicValue(3, true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ScryAction(magicNumber));
        addToBot(new SimpleGridCardSelectBuilder(c -> true)
                .setCardGroup(cpr().discardPile).setAmount(1).setMsg(String.format(MSG[0], 1))
                .setAnyNumber(false).setCanCancel(false).setShouldMatchAll(true).setDisplayInOrder(false)
                .setManipulator(new GridCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int index, CardGroup cardGroup) {
                        moveToHand(card, cpr().discardPile);
                        return false;
                    }
                }));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(2);
    }
}
