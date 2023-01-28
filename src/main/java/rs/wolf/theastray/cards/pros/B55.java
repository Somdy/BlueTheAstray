package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.SequenceExhaustPower;
import rs.wolf.theastray.powers.unique.SequencePlayPower;

public class B55 extends AstrayProCard {
    public B55() {
        super(55, 1, CardTarget.SELF);
        setMagicValue(1, true);
        addTip(MSG[0], String.format(MSG[1], magicNumber));
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new SequenceExhaustPower(magicNumber)));
        if (upgraded) {
            addToBot(ApplyPower(s, s, new SequencePlayPower(magicNumber)));
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        addTip(MSG[2], String.format(MSG[3], magicNumber));
    }
}