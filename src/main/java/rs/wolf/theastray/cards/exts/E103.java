package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E103 extends AstrayExtCard {
    private boolean hasAnyMinion;
    
    public E103() {
        super(103, 1, 4, CardTarget.ALL_ENEMY);
        setMagicValue(1, true);
        setMagicalDerivative(true);
        exhaust = true;
        hasAnyMinion = false;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (hasAnyMinion) {
            if (!upgraded) {
                atbTmpAction(() -> getRandom(LMSK.GetAllExptMstr(m -> m.hasPower(MinionPower.POWER_ID)), cardRandomRng())
                        .ifPresent(m -> addToTop(new InstantKillAction(m))));
            } else {
                atbTmpAction(() -> {
                    for (AbstractMonster m : LMSK.GetAllExptMstr(m -> m.hasPower(MinionPower.POWER_ID))) {
                        addToTop(new InstantKillAction(m));
                    }
                });
            }
        }
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        hasAnyMinion = LMSK.HasAnyExptMonster(m -> m.hasPower(MinionPower.POWER_ID));
        target = hasAnyMinion ? CardTarget.ALL_ENEMY : CardTarget.NONE;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        hasAnyMinion = LMSK.HasAnyExptMonster(m -> m.hasPower(MinionPower.POWER_ID));
        target = hasAnyMinion ? CardTarget.ALL_ENEMY : CardTarget.NONE;
    }
    
    @Override
    public void triggerOnGlowCheck() {
        glowColor = hasAnyMinion ? GOLD_BORDER_GLOW_COLOR.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}
