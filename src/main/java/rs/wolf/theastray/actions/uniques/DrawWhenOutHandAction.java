package rs.wolf.theastray.actions.uniques;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import rs.wolf.theastray.abstracts.AstrayGameAction;

import java.util.function.Consumer;

public class DrawWhenOutHandAction extends AstrayGameAction {
    private final Consumer<Integer> whenOutHand;
    private boolean continueWhenFull;
    
    public DrawWhenOutHandAction(int drawAmount, Consumer<Integer> whenOutHand) {
        this.amount = drawAmount;
        this.whenOutHand = whenOutHand;
        continueWhenFull = false;
        actionType = ActionType.DRAW;
    }
    
    public DrawWhenOutHandAction continueWhenFull() {
        continueWhenFull = true;
        return this;
    }
    
    @Override
    public void update() {
        isDone = true;
        if (cpr().hand.size() < BaseMod.MAX_HAND_SIZE) {
            amount--;
            if (amount > 0) {
                addToTop(new DrawWhenOutHandAction(amount, whenOutHand));
            }
            addToTop(new DrawCardAction(1));
        } else if (whenOutHand != null) {
            whenOutHand.accept(amount);
            if (continueWhenFull) addToTop(new DrawCardAction(amount));
        }
    }
}