package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;

public class B31 extends AstrayProCard {
    public B31() {
        super(31, 2, CardTarget.ALL_ENEMY);
        setDamageValue(14, true);
        setMagicValue(1, true);
        setMagical(true);
        isMultiDamage = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        boolean hasMagical = s.hasPower(MagicPower.ID);
        addToBot(DamageAllEnemiesAction(s, AbstractGameAction.AttackEffect.FIRE, !hasMagical ? null : m -> {
            int stack = s.getPower(MagicPower.ID).amount * magicNumber;
            addToTop(ApplyPower(m, s, burntPower(m, s, stack)));
        }));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
        setPromosValue(1, true);
    }
}