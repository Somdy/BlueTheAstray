package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.tools.GridCardManipulator;
import rs.lazymankits.actions.utility.SimpleGridCardSelectBuilder;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.GlobalManaMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class B7 extends AstrayProCard {
    public B7() {
        super(7, 1, CardTarget.SELF);
        setMagicValue(1, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            addToBot(new DrawCardAction(s, magicNumber));
        }
        addToBot(new ModifyManaAction(1));
        addToBot(new SimpleGridCardSelectBuilder(TAUtils::IsMagical)
                .setCardGroup(cpr().drawPile, cpr().hand, cpr().discardPile)
                .setAmount(1).setMsg(String.format(MSG[finalBranch() == 1 ? 1 : 0], 1))
                .setAnyNumber(false).setCanCancel(false)
                .setManipulator(new GridCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int i, CardGroup cardGroup) {
                        CardGroup group = findCardGroupWhereCardIs(card);
                        assert group != null;
                        if (!upgraded || finalBranch() == 0) {
                            group.moveToDeck(card, false);
                        }
                        if (finalBranch() == 1) {
                            group.moveToHand(card);
                        }
                        return false;
                    }
                })
        );
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
                exhaust = false;
            });
            add(() -> {
                upgradeTexts(1);
                upgradeBaseCost(0);
            });
        }};
    }
}
