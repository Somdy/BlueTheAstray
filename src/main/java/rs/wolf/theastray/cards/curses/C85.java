package rs.wolf.theastray.cards.curses;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.wolf.theastray.cards.AstrayCurseCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;

public class C85 extends AstrayCurseCard {
    public C85() {
        super(85);
        addTip(ILLUSION_CARD);
        addTags(TACardEnums.ILLUSION);
    }
    
    @Override
    public boolean canPlay(AbstractCard card) {
        int count = 0;
        for (AbstractCard c : cpr().hand.group) {
            if (c.hasTag(TACardEnums.ILLUSION))
                count++;
            if (count >= 3) break;
        }
        if (count >= 3) {
            addToTop(new PressEndTurnButtonAction());
        }
        return super.canPlay(card);
    }
    
    @Override
    public void triggerWhenDrawn() {
        addToTop(new MakeTempCardInHandAction(CardMst.GetCard(85), 1));
    }
}