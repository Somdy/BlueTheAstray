package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.powers.unique.DeathPower;

public class E83 extends AstrayExtCard {
    public E83() {
        super(83, 1, 14, CardTarget.SELF);
        setMagicValue(9, true);
        setExtraMagicValue(1, true);
        setMagical(true);
        setStorage(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new DeathPower(s, magicNumber, getExtraMagic())));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(3);
    }
}