package rs.wolf.theastray.cards.pros;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;

public class B18 extends AstrayProCard {
    public B18() {
        super(18, 2, CardTarget.ENEMY);
        setDamageValue(20, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
    
    @Override
    public void onNeitherStorageTriggers() {
        atbTmpAction(() -> {
            if (cpr().hand.size() < BaseMod.MAX_HAND_SIZE && !purgeOnUse)
                cpr().discardPile.moveToHand(this);
            addToTop(new ModifyManaAction(getManaOnUse()));
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(4);
    }
}