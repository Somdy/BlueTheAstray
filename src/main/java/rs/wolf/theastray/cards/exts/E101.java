package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.powers.FrostPower;

public class E101 extends AstrayExtCard {
    public E101() {
        super(101, 1, 3, CardTarget.ENEMY);
        setDamageValue(7, true);
        setMagicValue(1, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(ApplyPower(t, s, frostPower(t, s, magicNumber)));
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realDamage = baseDamage;
        int realMagics = baseMagicNumber;
        if (mo.hasPower(FrostPower.ID)) {
            baseDamage *= 2;
            baseMagicNumber *= 2;
        }
        super.calculateCardDamage(mo);
        baseDamage = realDamage;
        baseMagicNumber = realMagics;
        isDamageModified = baseDamage != damage;
        isMagicNumberModified = baseMagicNumber != magicNumber;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
    }
}