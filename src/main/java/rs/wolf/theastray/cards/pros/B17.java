package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class B17 extends AstrayProCard {
    public B17() {
        super(17, 1, CardTarget.ENEMY);
        setDamageValue(6, true);
        setMagicValue(2, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(ApplyPower(t, s, frostPower(t, s, magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
    }
}