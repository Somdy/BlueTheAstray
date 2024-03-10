package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.CreationPower;
import rs.wolf.theastray.powers.unique.CreatorPower;

public class B61 extends AstrayProCard {
    public B61() {
        super(61, 1, CardTarget.SELF);
        setMagicValue(2, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, upgraded ? new CreatorPower(magicNumber) : new CreationPower(magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}