package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.BlossomPower;

public class B62 extends AstrayProCard {
    public B62() {
        super(62, 3, CardTarget.SELF);
        setMagicValue(1, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new BlossomPower(magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        isInnate = true;
    }
}