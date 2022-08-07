package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class StrikeTA extends AstrayProCard {
    public StrikeTA() {
        super(1, 1, CardTarget.ENEMY);
        setDamageValue(6, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.FIRE));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
    }
}