package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.powers.unique.GiantPower;

public class E84 extends AstrayExtCard {
    public E84() {
        super(84, 0, 14, CardTarget.SELF);
        setMagicValue(1, true);
        setExtraMagicValue(15, true);
        addTags(TACardEnums.ILLUSION);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new GiantPower(s, magicNumber, getExtraMagic())));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeExtraMagic(5);
    }
}