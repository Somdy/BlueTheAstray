package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlyingDaggerEffect;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.powers.BurntPower;
import rs.wolf.theastray.utils.GlobalManaMst;

public class E95 extends AstrayExtCard {
    public E95() {
        super(95, 1, 6, CardTarget.ENEMY);
        setDamageValue(0, true);
        setMagicValue(3, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        for (int i = 0; i < damage / magicNumber; i++) {
            addToBot(new VFXAction(new FlyingDaggerEffect(t.hb.cX, t.hb.cY, 0F, AbstractDungeon.getMonsters().shouldFlipVfx())));
        }
        addToBot(DamageAction(t, s, damage, AbstractGameAction.AttackEffect.NONE));
        if (!upgraded) {
            addToBot(new RemoveSpecificPowerAction(t, s, BurntPower.ID));
        }
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int real = baseDamage;
        if (mo.hasPower(BurntPower.ID)) {
            AbstractPower p = mo.getPower(BurntPower.ID);
            if (p != null) baseDamage += p.amount * magicNumber;
        }
        super.calculateCardDamage(mo);
        baseDamage = real;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void triggerOnGlowCheck() {
        glowColor = GlobalManaMst.FullMana() ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(4);
    }
}