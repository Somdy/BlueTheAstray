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
        int mod = Math.abs(amount);
        if (amount > 0)
            GlobalManaMst.GainMana(mod);
        if (amount < 0)
            GlobalManaMst.LoseMana(mod);
        isDone = true;
    }
}