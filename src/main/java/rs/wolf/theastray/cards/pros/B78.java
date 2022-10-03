package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B78 extends AstrayProCard {
    public B78() {
        super(78, 0, CardTarget.SELF);
        setMagicValue(1, true);
        setCanEnlighten(true);
        isInnate = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            atbTmpAction(() -> {
                int count = cpr().drawPile.getPowers().size();
                if (upgraded)
                    count += cpr().discardPile.getPowers().size();
                addToTop(new GainEnergyAction(count));
            });
        } else if (finalBranch() == 1) {
            atbTmpAction(() -> {
                int count = cpr().drawPile.getPowers().size();
                addToTop(new DrawCardAction(s, count));
            });
        }
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> upgradeTexts());
            add(() -> upgradeTexts(1));
        }};
    }
}
