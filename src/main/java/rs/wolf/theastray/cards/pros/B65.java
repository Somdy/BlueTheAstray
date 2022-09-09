package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.AshPower;

public class B65 extends AstrayProCard {
    public B65() {
        super(65, 2, CardTarget.SELF);
        setMagicValue(1, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new AshPower(magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(0);
    }
}