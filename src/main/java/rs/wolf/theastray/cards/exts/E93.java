package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.red.Whirlwind;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import rs.lazymankits.actions.utility.DamageCallbackBuilder;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E93 extends AstrayExtCard {
    public E93() {
        super(93, 1, 4, CardTarget.ENEMY);
        setDamageValue(11, true);
        setMagicValue(1, true);
        setExtraMagicValue(1, true);
        setMagical(true);
        isMultiDamage = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SFXAction("ATTACK_WHIRLWIND"));
        addToBot(new VFXAction(new WhirlwindEffect()));
        addToBot(DamageAllEnemiesAction(s, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE, c -> {
            if (c.lastDamageTaken <= 0) {
                addToTop(ApplyPower(c, s, new VulnerablePower(c, getExtraMagic(), false)));
                addToTop(ApplyPower(c, s, new WeakPower(c, magicNumber, false)));
            }
        }));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
        upgradeExtraMagic(1);
    }
}
