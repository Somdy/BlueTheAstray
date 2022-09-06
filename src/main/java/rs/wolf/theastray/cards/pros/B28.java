package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.actions.uniques.MagicMissilesAction;
import rs.wolf.theastray.cards.AstrayProCard;

public class B28 extends AstrayProCard {
    public B28() {
        super(28, 1, CardTarget.ALL_ENEMY);
        setDamageValue(6, true);
        setMagicValue(4, true);
        setExtraMagicValue(1, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new MagicMissilesAction(this, s, magicNumber, getExtraMagic(), 
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }
    
    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        super.applyPowers();
        setDamageValue(damage, true);
        applyMagicalPowers();
        baseDamage = realBase;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        super.calculateCardDamage(mo);
        setDamageValue(damage, true);
        calculateMagicalCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
    }
}