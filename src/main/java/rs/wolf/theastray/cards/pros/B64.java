package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.PhosphorusPower;
import rs.wolf.theastray.utils.GlobalIDMst;

public class B64 extends AstrayProCard {
    public B64() {
        super(64, 2, CardTarget.SELF);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new PhosphorusPower(1)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        isInnate = true;
    }
}