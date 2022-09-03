package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class B27 extends AstrayProCard {
    public B27() {
        super(27, 0, CardTarget.ALL_ENEMY);
        setDamageValue(4, true);
        setMagicValue(1, true);
        setMagical(true);
        isMultiDamage = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAllEnemiesAction(s, AbstractGameAction.AttackEffect.NONE));
        updateDescription(DESCRIPTION);
    }
    
    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        int size = getAllLivingMstrs().size();
        baseDamage += magicNumber * size;
        super.applyPowers();
        updateDescription(DESCRIPTION + String.format(MSG[0], multiDamage[0]));
        baseDamage = realBase;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
    }
}
