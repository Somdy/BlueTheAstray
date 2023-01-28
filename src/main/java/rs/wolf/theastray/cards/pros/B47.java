package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.tools.HandCardManipulator;
import rs.lazymankits.actions.utility.SimpleHandCardSelectBuilder;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B47 extends AstrayProCard {
    public B47() {
        super(47, 1, CardTarget.SELF);
        setMagicValue(4, true);
        setBlockValue(6, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!cpr().hand.isEmpty()) {
            if (!upgraded || finalBranch() == 0) {
                atbTmpAction(() -> {
                    List<AbstractCard> tmp = new ArrayList<>(cpr().hand.group);
                    int size = Math.min(tmp.size(), magicNumber);
                    for (int i = 0; i < size; i++) {
                        AbstractCard c = tmp.remove(cardRandomRng().random(tmp.size() - 1));
                        addToTop(new ExhaustSpecificCardAction(c, cpr().hand));
                        if (tmp.isEmpty()) break;
                    }
                    addToTop(new GainBlockAction(s, size * block));
                });
            } else if (finalBranch() == 1) {
                addToBot(new SimpleHandCardSelectBuilder(c -> true).setAmount(magicNumber).setMsg(MSG[0])
                        .setAnyNumber(false).setCanPickZero(false).setManipulator(new HandCardManipulator() {
                            @Override
                            public boolean manipulate(AbstractCard card, int index) {
                                addToTop(new GainBlockAction(s, block));
                                addToTop(new ExhaustSpecificCardAction(card, cpr().hand));
                                return true;
                            }
                        }));
            }
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
                upgradeBlock(3);
            });
            add(() -> {
                upgradeTexts(1);
            });
        }};
    }
}