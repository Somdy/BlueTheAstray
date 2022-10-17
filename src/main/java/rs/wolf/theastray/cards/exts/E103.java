package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.cards.AstrayExtCard;

import java.util.List;

public class E103 extends AstrayExtCard {
    private boolean hasAnyMinion;
    
    public E103() {
        super(103, 0, 4, CardTarget.ALL_ENEMY);
        setMagicValue(1, true);
        setMagicalDerivative(true);
        exhaust = true;
        hasAnyMinion = false;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (hasAnyMinion) {
            if (!upgraded) {
                atbTmpAction(() -> {
                    List<AbstractMonster> list = LMSK.GetAllExptMstr(m -> m.hasPower(MinionPower.POWER_ID));
                    if (!list.isEmpty()) {
                        if (list.size() > magicNumber) {
                            int diff = list.size() - magicNumber;
                            for (int i = 0; i < diff; i++) {
                                list.remove(cardRandomRng().random(list.size() - 1));
                            }
                        }
                        for (AbstractMonster m : list) {
                            addToTop(new InstantKillAction(m));
                            addToTop(new VFXAction(new ClashEffect(m.hb.cX, m.hb.cY)));
                        }
                    }
                });
            } else {
                atbTmpAction(() -> {
                    for (AbstractMonster m : LMSK.GetAllExptMstr(m -> m.hasPower(MinionPower.POWER_ID))) {
                        addToTop(new InstantKillAction(m));
                        addToTop(new VFXAction(new ClashEffect(m.hb.cX, m.hb.cY)));
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
