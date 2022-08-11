package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class B12 extends AstrayProCard {
    public B12() {
        super(12, 1, CardTarget.SELF);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 1) {
            atbTmpAction(() -> {
                if (upgraded && finalBranch() == 1)
                    addToTop(new GainBlockAction(s, s, block));
                Optional<AbstractPower> p = getExptRandomPower(s.powers, cardRandomRng(),
                        po -> isPowerTypeOf(po, AbstractPower.PowerType.DEBUFF));
                p.ifPresent(po -> addToTop(new RemoveSpecificPowerAction(s, s, po)));
            });
            return;
        }
        if (finalBranch() == 0) {
            addToBot(new RemoveAllPowersAction(s, true));
        }
    }
    
    @Override
    public void onPlayerReceivePower(AbstractPower power, AbstractCreature source) {
        super.onPlayerReceivePower(power, source);
        if (inHand()) return;
        if (isPowerTypeOf(power, AbstractPower.PowerType.DEBUFF)) {
            atbTmpAction(() -> {
                findCardGroupWhereCardIs(this).moveToHand(this);
                applyPowers();
                cpr().hand.refreshHandLayout();
                cpr().hand.glowCheck();
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
                selfRetain = true;
            });
            add(() -> {
                upgradeTexts(1);
                setBlockValue(11, true);
            });
        }};
    }
}