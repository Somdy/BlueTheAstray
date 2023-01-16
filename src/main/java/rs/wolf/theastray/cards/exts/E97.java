package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayExtCard;

import java.util.ArrayList;
import java.util.List;

public class E97 extends AstrayExtCard {
    public E97() {
        super(97, 1, 8, CardTarget.NONE);
        setMagicValue(1, true);
        setMagicalDerivative(true);
        setStorage(true);
        exhaust = true;
        cardsToPreview = new Discovery();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            Discovery card = new Discovery();
            card.freeToPlayOnce = true;
            addToTop(new MakeTempCardInHandAction(card, magicNumber));
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts(1);
        upgradeBaseCost(0);
    }
}
