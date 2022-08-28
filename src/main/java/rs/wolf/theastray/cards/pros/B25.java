package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.PurityPower;

public class B25 extends AstrayProCard {
    public B25() {
        super(25, 2, CardTarget.SELF);
        setBlockValue(5, true);
        setMagicValue(25, true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
        addToBot(ApplyPower(s, s, new PurityPower(s, 1, magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBlock(3);
        upgradeMagicNumber(7);
    }
}
