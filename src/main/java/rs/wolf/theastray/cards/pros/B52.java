package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.wolf.theastray.cards.AstrayProCard;

public class B52 extends AstrayProCard {
    public B52() {
        super(52, 1, CardTarget.ENEMY);
        setDamageValue(6, true);
        setMagicValue(1, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(ApplyPower(t, s, new VulnerablePower(t, magicNumber, false)));
        addToBot(ApplyPower(t, s, new WeakPower(t, magicNumber, false)));
        addToBot(ApplyPower(t, s, burntPower(t, s, magicNumber)));
        addToBot(ApplyPower(t, s, frostPower(t, s, magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
    }
}