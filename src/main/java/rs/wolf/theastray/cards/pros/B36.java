package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.FrostPower;
import rs.wolf.theastray.powers.unique.FrostShieldPower;

public class B36 extends AstrayProCard {
    public B36() {
        super(36, 2, CardTarget.SELF);
        setBlockValue(14, true);
        setMagical(true);
        setStorage(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int baseBlocks = block;
            for (AbstractMonster m : getAllLivingMstrs()) {
                if (m.hasPower(FrostPower.ID)) {
                    AbstractPower p = m.getPower(FrostPower.ID);
                    assert p != null;
                    baseBlocks += p.amount;
                }
            }
            addToTop(new GainBlockAction(s, baseBlocks));
        });
        if (upgraded) {
            addToBot(ApplyPower(s, s, new FrostShieldPower(s, makeCopy(), 1)));
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}