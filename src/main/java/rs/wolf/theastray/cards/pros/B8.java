package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.tools.HandCardManipulator;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.actions.utility.SimpleHandCardSelectBuilder;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B8 extends AstrayProCard {
    public B8() {
        super(8, 1, CardTarget.NONE);
        setMagicValue(1, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleHandCardSelectBuilder(c -> true)
                .setAmount(1).setMsg(MSG[0])
                .setAnyNumber(false).setCanPickZero(false)
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int i) {
                        addToTop(new QuickAction(() -> {
                            int count = 0;
                            for (AbstractCard c : cpr().hand.group) {
                                if (isCardTypeOf(c, card.type)) {
                                    count++;
                                    addToTop(new DiscardSpecificCardAction(c));
                                }
                            }
                            if (count > 0) {
                                addToTop(new DrawCardAction(s, count));
                            }
                        }));
                        return true;
                    }
                }));
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
                upgradeBaseCost(0);
            });
            add(() -> {
                upgradeTexts(1);
                exhaust = false;
            });
        }};
    }
}