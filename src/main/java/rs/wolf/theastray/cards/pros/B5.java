package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.wolf.theastray.cards.AstrayProCard;

public class B5 extends AstrayProCard {
    public B5() {
        super(5, 1, CardTarget.ALL_ENEMY);
        setMagicValue(2, true);
        setExtraMagicValue(2, true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        for (AbstractMonster m : getAllLivingMstrs()) {
            addToBot(ApplyPower(m, s, new WeakPower(m, magicNumber, false)));
            if (upgraded)
                addToBot(ApplyPower(m, s, new StrengthPower(m, -getExtraMagic())));
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}