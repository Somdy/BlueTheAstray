package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.BurntPower;

import java.util.ArrayList;
import java.util.List;

public class B14 extends AstrayProCard {
    public B14() {
        super(14, 2, CardTarget.ENEMY);
        setDamageValue(15, true);
        setMagicValue(1, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.FIRE));
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int real = baseDamage;
        if (mo.hasPower(BurntPower.ID)) {
            baseDamage *= magicNumber;
        }
        super.calculateCardDamage(mo);
        baseDamage = real;
        isDamageModified = baseDamage != damage;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(1);
    }
    
//    @Override
//    protected List<UpgradeBranch> branches() {
//        return new ArrayList<UpgradeBranch>() {{
//            add(() -> {
//                upgradeTexts();
//                upgradeDamage(5);
//                upgradePromos(2);
//            });
//            add(() -> {
//                upgradeTexts(1);
//                upgradeDamage(5);
//                setMagicalDerivative(true);
//            });
//        }};
//    }
}