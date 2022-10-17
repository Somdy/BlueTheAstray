package rs.wolf.theastray.cards.curses;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.wolf.theastray.cards.AstrayCurseCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

public class C85 extends AstrayCurseCard {
    private static boolean EndingTurn = false;
    
    public C85() {
        super(85);
        addTip(ILLUSION_CARD);
        addTags(TACardEnums.ILLUSION);
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        if (!outOfDungeon() && TAUtils.RoomChecker(AbstractRoom.RoomPhase.COMBAT) && !EndingTurn) {
            int count = 0;
            for (AbstractCard c : cpr().hand.group) {
                if (c.hasTag(TACardEnums.ILLUSION))
                    count++;
                if (count >= 3) break;
            }
            if (count >= 3) {
                EndingTurn = true;
                addToTop(new PressEndTurnButtonAction());
            }
        }
    }
    
    @Override
    public void triggerOnEndOfPlayerTurn() {
        EndingTurn = false;
    }
    
    @Override
    public void triggerWhenDrawn() {
        addToTop(new MakeTempCardInHandAction(CardMst.GetCard(85), 1));
    }
}