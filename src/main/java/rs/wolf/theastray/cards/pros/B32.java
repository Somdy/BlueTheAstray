package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.common.DiscoverAction;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.curses.C85;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class B32 extends AstrayProCard {
    public B32() {
        super(32, 1, CardTarget.NONE);
        setMagicValue(1, true);
        setExtraMagicValue(1, true);
        setCanEnlighten(true);
        exhaust = true;
        addTip(ILLUSION_CARD);
        cardsToPreview = new C85();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            addToBot(new DiscoverAction(DiscoverAction.Generate(3, CardMst
                    .ReturnAllCardsInCombat(c -> c.hasTag(TACardEnums.ILLUSION), true), c -> true)));
            if (!upgraded) {
                addToBot(new MakeTempCardInDrawPileAction(CardMst.GetCard("镜中幻象"), 1,
                        true, true));
            }
        } else if (finalBranch() == 1) {
            for (int i = 0; i < magicNumber; i++) {
                AbstractCard card = CardMst.ReturnRndCardInCombat(c -> c.hasTag(TACardEnums.ILLUSION), true);
                addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
            }
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
                cardsToPreview = null;
            });
            add(() -> {
                upgradeTexts(1);
                upgradeBaseCost(0);
                cardsToPreview = null;
            });
        }};
    }
}