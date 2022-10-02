package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.common.DiscoverAction;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E105 extends AstrayExtCard {
    public E105() {
        super(105, 0, 1, CardTarget.SELF);
        setMagicValue(1, true);
        setMagicalDerivative(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new HealAction(s, s, magicNumber));
        addToBot(new DiscoverAction(DiscoverAction.Generate(3, c -> true)));
    }
    
    @Override
    public boolean canUpgrade() {
        return false;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}
