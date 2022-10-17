package rs.wolf.theastray.actions.commons;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.wolf.theastray.abstracts.AstrayGameAction;

public class DrawPileToHandAction extends AstrayGameAction {
    private final AbstractCard card;
    
    public DrawPileToHandAction(AbstractCard card) {
        this.card = card;
        actionType = ActionType.CARD_MANIPULATION;
    }
    
    @Override
    public void update() {
        isDone = true;
        if (cpr().drawPile.contains(card) && cpr().hand.size() < BaseMod.MAX_HAND_SIZE) {
            cpr().drawPile.moveToHand(card);
            cpr().hand.glowCheck();
        }
    }
}
