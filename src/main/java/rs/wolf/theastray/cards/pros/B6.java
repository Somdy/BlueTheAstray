package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.utils.GlobalManaMst;

public class B6 extends AstrayProCard {
    public B6() {
        super(6, 1, CardTarget.SELF);
        setBlockValue(5, true);
        setMagicValue(3, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        int totalBlock = block + GlobalManaMst.CurrentMana() * magicNumber;
        addToBot(new GainBlockAction(s, totalBlock));
        updateDescription(DESCRIPTION);
        
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        int totalBlock = block + GlobalManaMst.CurrentMana() * magicNumber;
        updateDescription(DESCRIPTION + String.format(MSG[0], totalBlock));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}