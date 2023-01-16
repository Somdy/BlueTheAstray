package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.actions.utility.DamageCallbackBuilder;
import rs.wolf.theastray.cards.AstrayProCard;

public class B49 extends AstrayProCard {
    private final int diff;
    
    public B49() {
        super(49, 1, CardTarget.SELF_AND_ENEMY);
        setDamageValue(18, true);
        setMagicValue(3, true);
        diff = baseDamage - baseMagicNumber;
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            atbTmpAction(() -> {
                int golds = cardRandomRng().random(3, 18);
                addToTop(DamageAction(t, s, golds, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToTop(new GainGoldAction(golds));
            });
        } else {
            atbTmpAction(() -> {
                int dmg = magicNumber <= damage ? cardRandomRng().random(magicNumber, damage) : damage;
                addToTop(new DamageCallbackBuilder(t, crtDmgInfo(s, dmg, damageTypeForTurn), 
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT, c -> {
                    if (c.lastDamageTaken > 0)
                        addToTop(new GainGoldAction(c.lastDamageTaken));
                }));
            });
        }
        addToBot(new DrawCardAction(s, 1));
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        int gap = Math.abs(damage - magicNumber) - diff;
        magicNumber += gap;
        if (magicNumber < 0)
            magicNumber = 0;
        isMagicNumberModified = magicNumber != baseMagicNumber;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        int gap = Math.abs(damage - magicNumber) - diff;
        magicNumber += gap;
        if (magicNumber < 0)
            magicNumber = 0;
        isMagicNumberModified = magicNumber != baseMagicNumber;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}
