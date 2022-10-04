package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.powers.unique.InfWisdomPower;

public class E106 extends AstrayExtCard {
    public E106() {
        super(106, 1, 15, CardTarget.SELF);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new InfWisdomPower(1)));
    }
    
    @Override
    public boolean canUpgrade() {
        return false;
    }
    
    @Override
    public void selfUpgrade() {}
}