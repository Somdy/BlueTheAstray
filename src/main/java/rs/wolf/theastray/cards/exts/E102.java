package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.actions.uniques.DrawWhenOutHandAction;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E102 extends AstrayExtCard {
    public E102() {
        super(102, 0, 4, CardTarget.NONE);
        setMagicValue(3, true);
        setMagicalDerivative(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawWhenOutHandAction(magicNumber, left -> addToTop(new GainEnergyAction(left))).continueWhenFull());
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
    }
}