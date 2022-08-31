package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.WizardFieldPower;

public class B40 extends AstrayProCard {
    public B40() {
        super(40, 1, CardTarget.SELF);
        setBlockValue(7, true);
        setMagicValue(14, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
        addToBot(ApplyPower(s, s, new WizardFieldPower(s, magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBlock(3);
        upgradeMagicNumber(6);
    }
}