package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.BufferPower;
import rs.wolf.theastray.cards.AstrayProCard;

public class B66 extends AstrayProCard {
    private int counter;
    
    public B66() {
        super(66, -2, CardTarget.ALL);
        setMagicValue(1, true);
        setDamageValue(17, true);
        addTip(MSG[0], MSG[1]);
        selfRetain = true;
        counter = 0;
    }
    
    @Override
    public void onRetained() {
        counter++;
        if (counter >= 2) {
            addToTop(new ExhaustSpecificCardAction(this, cpr().hand));
        }
    }
    
    @Override
    public void triggerOnExhaust() {
        addToTop(DamageAllEnemiesAction(cpr(), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToTop(ApplyPower(cpr(), cpr(), new BufferPower(cpr(), magicNumber)));
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void selfUpgrade() {}
}
