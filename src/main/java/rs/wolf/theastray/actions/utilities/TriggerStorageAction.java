package rs.wolf.theastray.actions.utilities;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.curses.Pain;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.actions.commons.ModifyManaAction;

public class TriggerStorageAction extends AstrayGameAction {
    private boolean[] triggers;
    
    public TriggerStorageAction(boolean energy, boolean mana) {
        triggers = new boolean[] {energy, mana, energy && mana};
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        if (triggers[0])
            addToTop(new GainEnergyAction(1));
        if (triggers[1])
            addToTop(new ModifyManaAction(1));
        if (triggers[2])
            addToTop(new MakeTempCardInHandAction(new Pain()));
        isDone = true;
    }
}