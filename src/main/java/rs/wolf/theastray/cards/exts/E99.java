package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.powers.FrostPower;

public class E99 extends AstrayExtCard {
    public E99() {
        super(99, 1, 5, CardTarget.ALL_ENEMY);
        setMagicValue(5, true);
        setMagical(true);
        setStorage(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            for (AbstractMonster m : getAllExptMstrs(m -> !m.isDeadOrEscaped() && !m.hasPower(FrostPower.ID))) {
                addToTop(ApplyPower(m, s, frostPower(m, s, magicNumber)));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(2);
    }
}
