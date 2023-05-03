package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.powers.StoragePower;

public class B18 extends AstrayProCard {
    public B18() {
        super(18, 2, CardTarget.ENEMY);
        setDamageValue(20, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        atbTmpAction(() -> {
            int count = 0;
            AbstractPower p = StoragePower.GetInstance(true);
            if (p instanceof StoragePower) {
                ((StoragePower) p).instant();
                count++;
            }
            p = StoragePower.GetInstance(false);
            if (p instanceof StoragePower) {
                ((StoragePower) p).instant();
                count++;
            }
            if (count >= 2) {
                addToTop(new MakeTempCardInHandAction(CardMst.GetCard("短路")));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(4);
    }
}