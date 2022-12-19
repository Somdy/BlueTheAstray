package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;

public class B22 extends AstrayProCard {
    public B22() {
        super(22, 0, CardTarget.ENEMY);
        setDamageValue(8, true);
        setMagical(true);
//        returnToHand = true;
        setReturnToHand(true);
        isEthereal = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        attTmpAction(() -> {
            int rCost = cardRandomRng().random(3);
            if (costForTurn != rCost) setCostForTurn(rCost);
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
    }
    
    @Override
    protected int selfModifyManaOnUse(int manaCost) {
        return 0;
    }
    
    @Override
    public void triggerOnExhaust() {
        addToTop(new ModifyManaAction(-getManaCost()));
    }
}