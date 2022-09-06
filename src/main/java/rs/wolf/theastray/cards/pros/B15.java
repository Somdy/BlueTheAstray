package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B15 extends AstrayProCard {
    public B15() {
        super(15, 1, CardTarget.SELF);
        setBlockValue(6, true);
        setCanEnlighten(true);
        selfRetain = true;
        returnToHand = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
        setCostValue(1, true);
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                upgradeTexts();
                upgradeBlock(2);
            });
            add(() -> {
                upgradeTexts(1);
                setMagical(true);
            });
        }};
    }
}