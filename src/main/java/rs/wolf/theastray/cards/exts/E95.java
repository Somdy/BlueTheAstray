package rs.wolf.theastray.cards.exts;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlyingDaggerEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.utils.GlobalManaMst;

public class E95 extends AstrayExtCard {
    public E95() {
        super(95, 1, 2, CardTarget.ENEMY);
        setDamageValue(9, true);
        setMagicValue(40, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (GlobalManaMst.FullMana()) {
            addToBot(new VFXAction(new WeightyImpactEffect(t.hb.cX, t.hb.cY, Color.ORANGE.cpy())));
        } else {
            addToBot(new VFXAction(new FlyingDaggerEffect(t.hb.cX, t.hb.cY, 0F, AbstractDungeon.getMonsters().shouldFlipVfx())));
        }
        addToBot(DamageAction(t, s, damage, AbstractGameAction.AttackEffect.NONE));
    }
    
    @Override
    public void applyPowers() {
        int real = baseDamage;
        if (GlobalManaMst.FullMana())
            baseDamage += magicNumber;
        super.applyPowers();
        baseDamage = real;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int real = baseDamage;
        if (GlobalManaMst.FullMana())
            baseDamage += magicNumber;
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
        upgradeMagicNumber(10);
    }
}