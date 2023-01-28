package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.patches.TACardEnums;

import java.util.ArrayList;
import java.util.List;

public class B48 extends AstrayProCard {
    public B48() {
        super(48, 1, CardTarget.NONE);
        setMagicValue(1, true);
        setCanEnlighten(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                for (AbstractCard card : DrawCardAction.drawnCards) {
                    if (card.hasTag(TACardEnums.MAGICAL)) {
                        if (!upgraded || finalBranch() == 0) {
                            addToTop(new ModifyManaAction(1));
                        } else if (finalBranch() == 1) {
                            addToTop(new GainEnergyAction(1));
                        }
                    } else if (!upgraded || finalBranch() == 0) {
                        addToTop(new GainEnergyAction(1));
                    }
                }
            }
        }));
        if (!upgraded || finalBranch() == 0) {
            addToBot(new DiscardAction(s, s, upgraded ? 2 : 1, false));
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
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(2);
            });
        }};
    }
}