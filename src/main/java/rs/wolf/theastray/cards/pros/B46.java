package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;

public class B46 extends AstrayProCard {
    private static boolean playedOnceInCombat = false;
    private boolean fake;
    
    public B46() {
        super(46, 2, CardTarget.SELF);
        setMagicValue(1, true);
        cardsToPreview = new B46(UPDATED_DESC[0]);
        exhaust = true;
        fake = false;
    }
    
    public B46(String updated) {
        super(46, 0, CardTarget.NONE);
        updateDescription(updated);
        exhaust = true;
        fake = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (fake || playedOnceInCombat) {
            addToBot(new DrawCardAction(s, 1));
            return;
        }
        int amount = magicNumber * AbstractDungeon.bossCount;
        addToBot(ApplyPower(s, s, new MagicPower(s, amount)));
        playedOnceInCombat = true;
    }
    
    @Override
    public void applyPowers() {
        if (playedOnceInCombat) {
            updateDescription(UPDATED_DESC[0]);
            setCostValue(0, true);
            target = CardTarget.NONE;
            exhaust = true;
            fake = true;
        }
        super.applyPowers();
    }
    
    @Override
    public void onVictory() {
        playedOnceInCombat = false;
    }
    
    @Override
    public void selfUpgrade() {
        if (!fake) {
            upgradeTexts();
            isInnate = true;
        }
    }
}
