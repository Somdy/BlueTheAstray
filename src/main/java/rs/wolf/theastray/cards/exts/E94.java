package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.actions.tools.GridCardManipulator;
import rs.lazymankits.actions.utility.SimpleGridCardSelectBuilder;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.utils.GlobalManaMst;

public class E94 extends AstrayExtCard {
    public E94() {
        super(94, 1, 10, CardTarget.ENEMY);
        setDamageValue(5, true);
        setMagicValue(1, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        int times = magicNumber * GlobalManaMst.CurrentMana();
        Leader.devLog("[" + name + "] DEALS [" + times + "] TIMES DAMAGE IN TOTAL");
        for (int i = 0; i < times; i++) {
            addToBot(DamageAction(t, s, getRandom(listFromObjs(AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)).orElse(AbstractGameAction.AttackEffect.SLASH_VERTICAL)));
        }
        updateDescription(DESCRIPTION);
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        int times = magicNumber * GlobalManaMst.CurrentMana();
        updateDescription(DESCRIPTION + String.format(MSG[0], damage, times));
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        int times = magicNumber * GlobalManaMst.CurrentMana();
        updateDescription(DESCRIPTION + String.format(MSG[0], damage, times));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
    }
}