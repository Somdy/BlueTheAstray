package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.AmendmentPower;

public class B56 extends AstrayProCard {
    public B56() {
        super(56, 1, CardTarget.SELF);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new AmendmentPower()));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        isInnate = true;
    }
}