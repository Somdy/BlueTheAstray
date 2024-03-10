package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.FrostShieldPower;

public class B36 extends AstrayProCard {
    public B36() {
        super(36, 1, CardTarget.SELF);
        setBlockValue(6, true);
        setMagical(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
        addToBot(ApplyPower(s, s, new FrostShieldPower(s, 1)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
//        upgradeBlock(3);
        exhaust = false;
    }
}