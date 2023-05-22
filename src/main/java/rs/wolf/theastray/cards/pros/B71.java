package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.EvilMouthPower;
import rs.wolf.theastray.powers.unique.EvilWordPower;

import java.util.ArrayList;
import java.util.List;

public class B71 extends AstrayProCard {
    public B71() {
        super(71, 1, CardTarget.SELF);
        setMagicValue(1, true);
        setCanEnlighten(true);
//        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            addToBot(ApplyPower(s, s, new EvilMouthPower(magicNumber, 1)));
        } else if (finalBranch() == 1) {
            addToBot(ApplyPower(s, s, new EvilWordPower(magicNumber, 1)));
        }
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>(){{
            add(() -> {
                upgradeTexts();
                upgradeMagicNumber(1);
                setStorage(true);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeBaseCost(0);
                setStorage(true);
            });
        }};
    }
}