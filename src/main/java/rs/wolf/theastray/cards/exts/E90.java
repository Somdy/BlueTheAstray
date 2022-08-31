package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.powers.FrostPower;

import java.util.List;

public class E90 extends AstrayExtCard {
    public E90() {
        super(90, 2, 4, CardTarget.ALL_ENEMY);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            List<AbstractMonster> tmp = getAllExptMstrs(m -> m.hasPower(FrostPower.ID) && !m.isDeadOrEscaped());
            if (!tmp.isEmpty()) {
                for (AbstractMonster m : tmp) {
                    AbstractPower p = m.getPower(FrostPower.ID);
                    if (p != null) {
                        int amount = p.amount;
                        if (upgraded) {
                            addToTop(ApplyPower(m, s, new WeakPower(m, amount, false)));
                        }
                        addToTop(ApplyPower(m, s, new StrengthPower(m, -amount)));
                        addToTop(new RemoveSpecificPowerAction(m, s, p));
                    }
                }
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}
