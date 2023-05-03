package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rs.wolf.theastray.cards.AstrayProCard;

public class B33 extends AstrayProCard {
    public B33() {
        super(33, 1, CardTarget.NONE);
        setStorage(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            if (!cpr().drawPile.isEmpty()) {
                AbstractCard topCard = cpr().drawPile.getTopCard().makeStatEquivalentCopy();
                topCard.modifyCostForCombat(-9);
                topCard.lighten(false);
                topCard.current_x = Settings.WIDTH / 2F;
                topCard.current_y = Settings.HEIGHT / 2F;
                topCard.applyPowers();
                cpr().hand.addToHand(topCard);
                cpr().hand.refreshHandLayout();
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(0);
    }
}