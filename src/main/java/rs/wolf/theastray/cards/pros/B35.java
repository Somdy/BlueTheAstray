package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayProCard;

public class B35 extends AstrayProCard {
    public B35() {
        super(35, 2, CardTarget.ALL);
        setBlockValue(12, true);
        setMagicValue(2, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
        atbTmpAction(() -> {
            for (AbstractMonster m : getAllLivingMstrs()) {
                addToTop(ApplyPower(m, s, frostPower(m, s, magicNumber)));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBlock(4);
        upgradeMagicNumber(2);
    }
}
