package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.BurntPower;
import rs.wolf.theastray.powers.FrostPower;

import java.util.List;

public class B51 extends AstrayProCard {
    public B51() {
        super(51, 2, CardTarget.ALL_ENEMY);
        setMagicValue(2, true);
        setExtraMagicValue(4, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            List<AbstractMonster> monsters = getAllExptMstrs(m -> !m.isDeadOrEscaped()
                    && (m.hasPower(BurntPower.ID) || m.hasPower(FrostPower.ID)));
            if (!monsters.isEmpty()) {
                int count = 0;
                for (AbstractMonster m : monsters) {
                    AbstractPower b = m.getPower(BurntPower.ID);
                    if (b != null) count += b.amount;
                    b = m.getPower(FrostPower.ID);
                    if (b != null) count += b.amount;
                }
                setDamageValue(count * magicNumber, true);
                for (AbstractMonster m : monsters) {
                    addToTop(ApplyPower(m, s, burntPower(m, s, getExtraMagic())));
                    if (upgraded) {
                        addToTop(ApplyPower(m, s, frostPower(m, s, getExtraMagic())));
                    }
                    calculateCardDamage(m);
                    addToTop(DamageAction(m, s, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
    }
}