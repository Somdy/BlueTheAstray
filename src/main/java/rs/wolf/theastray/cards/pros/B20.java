package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class B20 extends AstrayProCard {
    public B20() {
        super(20, 0, CardTarget.ALL_ENEMY);
        setDamageValue(4, true);
        setMagicValue(4, true);
        setMagical(true);
        setStorage(true);
        isMultiDamage = true;
        isInnate = true;
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAllEnemiesAction(s, AbstractGameAction.AttackEffect.FIRE, 
                m -> addToTop(ApplyPower(m, s, burntPower(m, s, magicNumber)))));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
        upgradeMagicNumber(2);
    }
}