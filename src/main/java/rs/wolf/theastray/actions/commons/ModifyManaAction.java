package rs.wolf.theastray.actions.commons;

import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.utils.GlobalManaMst;

public class ModifyManaAction extends AstrayGameAction {
    
    public ModifyManaAction(int amount) {
        this.amount = amount;
        actionType = ActionType.ENERGY;
    }
    
    @Override
    public void update() {
        if (amount > 0)
            GlobalManaMst.GainMana(amount);
        if (amount < 0)
            GlobalManaMst.LoseMana(-amount);
        isDone = true;
    }
}