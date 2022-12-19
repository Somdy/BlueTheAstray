package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import rs.wolf.theastray.cards.AstrayProCard;

public class B66 extends AstrayProCard {
    private int counter;
    
    public B66() {
        super(66, -2, CardTarget.ALL);
        setMagicValue(1, true);
        setDamageValue(17, true);
        addTip(MSG[0], MSG[1]);
        selfRetain = true;
        counter = 0;
        damageType = DamageInfo.DamageType.THORNS;
        isMultiDamage = true;
    }
    
    @Override
    public void onRetained() {
        counter++;
        if (counter >= 2) {
            addToTop(new ExhaustSpecificCardAction(this, cpr().hand));
        }
    }
    
    @Override
    public void triggerOnExhaust() {
        addToTop(DamageAllEnemiesAction(cpr(), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToTop(ApplyPower(cpr(), cpr(), new BufferPower(cpr(), magicNumber)));
    }
    
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }
    
    @Override
    public boolean canUpgrade() {
        return false;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void selfUpgrade() {}
}
