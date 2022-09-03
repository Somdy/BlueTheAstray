package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class B50 extends AstrayProCard {
    public B50() {
        super(50, 2, CardTarget.SELF_AND_ENEMY);
        setDamageValue(3, true);
        setBlockValue(3, true);
        setPromosValue(0, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, damage > 10 ? AbstractGameAction.AttackEffect.SLASH_HEAVY
                : AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(s, block));
        atbTmpAction(() -> {
            upgradePromos(1);
            updateDescription(UPDATED_DESC[0]);
        });
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        if (getPromos() > 0)
            updateDescription(UPDATED_DESC[0]);
    }
    
    @Override
    public void selfUpgrade() {
        upgradeName();
        upgradeBaseCost(1);
    }
    
    @Override
    protected int selfModifyManaOnUse(int manaCost) {
        return 0;
    }
}
