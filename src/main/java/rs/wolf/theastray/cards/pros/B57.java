package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.CoordinationPower;

public class B57 extends AstrayProCard {
    public B57() {
        super(57, 1, CardTarget.SELF);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ModifyManaAction(1));
        addToBot(ApplyPower(s, s, new CoordinationPower(1)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(0);
    }
}