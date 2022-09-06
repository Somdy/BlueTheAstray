package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayProCard;

public class B27 extends AstrayProCard {
    public B27() {
        super(27, 0, CardTarget.ALL_ENEMY);
        setDamageValue(4, true);
        setMagicValue(3, true);
        setMagical(true);
        isMultiDamage = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAllEnemiesAction(s, AbstractGameAction.AttackEffect.NONE));
    }
    
    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        int size = getAllLivingMstrs().size();
        baseDamage += magicNumber * size;
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        int size = getAllLivingMstrs().size();
        baseDamage += magicNumber * size;
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
    }
}
