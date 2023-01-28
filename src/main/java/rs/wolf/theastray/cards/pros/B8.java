package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.tools.HandCardManipulator;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.actions.utility.SimpleHandCardSelectBuilder;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class B8 extends AstrayProCard {
    public B8() {
        super(8, 0, CardTarget.NONE);
        setMagicValue(4, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Deprecated
    public void _play(AbstractCreature s, AbstractCreature t) {
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
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 1) {
            atbTmpAction(() -> {
                List<AbstractCard> cards = cpr().hand.group.stream().filter(c -> upgraded != TAUtils.IsMagical(c)).collect(Collectors.toList());
                if (cards.size() > 0) {
                    int count = cards.size();
                    for (AbstractCard c : cards) {
                        if (cpr().hand.contains(c)) {
                            cpr().hand.moveToDiscardPile(c);
                            GameActionManager.incrementDiscard(false);
                            c.triggerOnManualDiscard();
                        }
                    }
                    addToTop(new DrawCardAction(s, count));
                }
            });
        } else if (finalBranch() == 0) {
            atbTmpAction(() -> {
                List<AbstractCard> cards = new ArrayList<>();
                atbTmpAction(() -> {
                    if (cards.size() > 0) {
                        int count = Math.min(cards.size(), magicNumber);
                        for (AbstractCard c : cards) {
                            if (cpr().hand.contains(c)) {
                                cpr().hand.moveToDiscardPile(c);
                                GameActionManager.incrementDiscard(false);
                                c.triggerOnManualDiscard();
                            }
                        }
                        addToTop(new DrawCardAction(s, count));
                    }
                });
                addToTop(new SimpleHandCardSelectBuilder(c -> true).setAmount(cpr().hand.size()).setMsg(MSG[0])
                        .setAnyNumber(true).setCanPickZero(true).setManipulator(new HandCardManipulator() {
                            @Override
                            public boolean manipulate(AbstractCard card, int i) {
                                cards.add(card);
                                return true;
                            }
                        }));
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