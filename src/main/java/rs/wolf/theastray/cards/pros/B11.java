package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.wolf.theastray.cards.AstrayProCard;

public class B11 extends AstrayProCard {
    public B11() {
        super(11, 1, CardTarget.ENEMY);
        setDamageValue(9, true);
        setMagicValue(1, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (t instanceof AbstractMonster && ((AbstractMonster) t).getIntentBaseDmg() > 0)
            addToBot(ApplyPower(t, s, new WeakPower(t, magicNumber, false)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(5);
    }
}