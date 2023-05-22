package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.BlurPower;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.powers.unique.RemainPower;
import rs.wolf.theastray.powers.unique.StrandedPower;

import java.util.ArrayList;
import java.util.List;

public class E98 extends AstrayExtCard {
    public E98() {
        super(98, 2, 6, CardTarget.SELF);
        setMagicValue(2, true);
        setMagical(true);
        setCanEnlighten(true);
        selfRetain = true;
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int blocks = s.currentBlock * magicNumber;
            if (!upgraded || finalBranch() == 0) {
                addToTop(ApplyPower(s, s, new RemainPower(s, blocks)));
                if (upgraded) {
                    addToTop(ApplyPower(s, s, new BlurPower(s, getExtraMagic())));
                }
            } else if (finalBranch() == 1) {
                addToTop(ApplyPower(s, s, new StrandedPower(s, blocks)));
                addToTop(new PressEndTurnButtonAction());
            }
        });
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
                setExtraMagicValue(1, true);
                setStorage(true);
            });
            add(() -> {
                upgradeTexts(1);
                setStorage(true);
            });
        }};
    }
}