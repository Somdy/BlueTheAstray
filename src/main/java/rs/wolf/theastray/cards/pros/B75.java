package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class B75 extends AstrayProCard {
    public B75() {
        super(75, 1, CardTarget.NONE);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 1) {
            atbTmpAction(() -> {
                int count = 0;
                List<AbstractCard> tmp = new ArrayList<>();
                for (AbstractCard card : cpr().hand.group) {
                    if (TAUtils.IsMagical(card)) {
                        tmp.add(card);
                        count++;
                    }
                }
                addToTop(new ModifyManaAction(count));
                for (AbstractCard card : tmp) {
                    addToTop(new ExhaustSpecificCardAction(card, cpr().hand));
                }
            });
        } else if (finalBranch() == 0) {
            atbTmpAction(() -> {
                int size = cpr().hand.size();
                addToTop(new ModifyManaAction(size));
                for (AbstractCard card : cpr().hand.group) {
                    addToTop(new ExhaustSpecificCardAction(card, cpr().hand));
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
        return new ArrayList<UpgradeBranch>(){{
           add(() -> upgradeTexts());
           add(() -> {
               upgradeTexts(1);
               setMagicValue(2, true);
           });
        }};
    }
}
