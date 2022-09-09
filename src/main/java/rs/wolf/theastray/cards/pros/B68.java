package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.MeteoritePower;

public class B68 extends AstrayProCard {
    public B68() {
        super(68, 5, CardTarget.ALL_ENEMY);
        setDamageValue(60, true);
        setMagicValue(14, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (isInAutoplay) {
            addToBot(new QuickAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs()) {
                    addToTop(new ApplyPowerAction(m, s, burntPower(m, s, magicNumber)));
                }
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    addToTop(DamageAction(m, s, AbstractGameAction.AttackEffect.NONE));
                    addToTop(new VFXAction(new VerticalImpactEffect(m.hb.cX, m.hb.cY)));
                }
            }));
            return;
        }
        addToBot(ApplyPower(s, s, new MeteoritePower(damage, magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(15);
        upgradeMagicNumber(7);
    }
}
