package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.IgnorancePower;
import rs.wolf.theastray.powers.unique.IgnorantOnePower;

public class B60 extends AstrayProCard {
    public B60() {
        super(60, 1, CardTarget.SELF);
        setMagicValue(2, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            addToBot(ApplyPower(s, s, new IgnorancePower(magicNumber)));
        } else {
            addToBot(ApplyPower(s, s, new IgnorantOnePower(magicNumber)));
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}
