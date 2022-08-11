package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class DefendTA extends AstrayProCard {
    public DefendTA() {
        super(2, 1, CardTarget.SELF);
        setBlockValue(5, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBlock(3);
    }
}