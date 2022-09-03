package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.ManaFieldFastPower;
import rs.wolf.theastray.powers.unique.ManaFieldPower;

import java.util.ArrayList;
import java.util.List;

public class B54 extends AstrayProCard {
    public B54() {
        super(54, 1, CardTarget.SELF);
        setMagicValue(4, true);
        setCanEnlighten(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            addToBot(ApplyPower(s, s, new ManaFieldPower(magicNumber)));
        } else if (finalBranch() == 1) {
            addToBot(ApplyPower(s, s, new ManaFieldFastPower(magicNumber)));
        }
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
                upgradeMagicNumber(1);
            });
            add(() -> upgradeTexts(1));
        }};
    }
}