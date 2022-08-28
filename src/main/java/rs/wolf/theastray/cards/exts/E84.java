package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E84 extends AstrayExtCard {
    public E84() {
        super(84, 0, 14, CardTarget.SELF);
        setBlockValue(25, true);
        setMagicValue(1, true);
        setMagicalDerivative(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new VulnerablePower(s, magicNumber, false)));
        addToBot(new GainBlockAction(s, block));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBlock(7);
    }
}