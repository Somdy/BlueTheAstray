package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E104 extends AstrayExtCard {
    public E104() {
        super(104, 0, 7, CardTarget.SELF);
        setDamageValue(6, true);
        setMagicalDerivative(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(s, s, AbstractGameAction.AttackEffect.FIRE));
        addToBot(new HealAction(s, s, damage));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
    }
}
