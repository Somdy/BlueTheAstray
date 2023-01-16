package rs.wolf.theastray.actions.uniques;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.core.CardMst;

public class MeteoritesAutoStarAction extends AstrayGameAction {
    
    public MeteoritesAutoStarAction(int amount) {
        this.amount = amount;
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        isDone = true;
        if (amount <= 0) return;
        if (cpr().hand.size() < BaseMod.MAX_HAND_SIZE) {
            amount--;
            if (amount > 0) {
                addToTop(new MeteoritesAutoStarAction(amount));
            }
            addToTop(new MakeTempCardInHandAction(CardMst.GetCard("星星"), 1));
        } else {
            for (int i = 0; i < amount; i++) {
                addToTop(new NewQueueCardAction(CardMst.GetCard("星星"), true, true, true));
            }
        }
    }
}