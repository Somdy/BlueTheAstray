package rs.wolf.theastray.cards.pros;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B13 extends AstrayProCard {
    public B13() {
        super(13, 0, CardTarget.SELF);
        setMagicValue(2, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ModifyManaAction(magicNumber));
    }
    
    @Override
    public void onManaRunOut() {
        super.onManaRunOut();
        if ((!upgraded || finalBranch() == 0) && !inHand()) {
            attTmpAction(() -> {
                if (cpr().hand.size() < BaseMod.MAX_HAND_SIZE) {
                    findCardGroupWhereCardIs(this).moveToHand(this);
                    applyPowers();
                    cpr().hand.refreshHandLayout();
                    cpr().hand.glowCheck();
                }
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
            add(() -> {
                upgradeTexts();
                upgradeMagicNumber(1);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(2);
            });
        }};
    }
}