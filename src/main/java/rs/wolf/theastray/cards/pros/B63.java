package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.MagicalBodyPower;

public class B63 extends AstrayProCard {
    public B63() {
        super(63, 2, CardTarget.SELF);
        setBlockValue(5, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new MagicalBodyPower(s, block)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(1);
    }
}