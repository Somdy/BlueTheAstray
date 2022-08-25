package rs.wolf.theastray.actions.utilities;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Pain;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.actions.commons.ModifyManaAction;

public class TriggerStorageAction extends AstrayGameAction {
    private boolean[] triggers;
    private AbstractCard card;
    
    public TriggerStorageAction(boolean energy, boolean mana, AbstractCard card) {
        triggers = new boolean[] {energy, mana, energy && mana, !energy && !mana};
        this.card = card;
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        if (triggers[3]) {
            if (card != null && card instanceof AstrayCard)
                ((AstrayCard) card).onNeitherStorageTriggers();
            isDone = true;
            return;
        }
        if (triggers[0])
            addToTop(new GainEnergyAction(1));
        if (triggers[1])
            addToTop(new ModifyManaAction(1));
        if (triggers[2])
            addToTop(new MakeTempCardInHandAction(new Pain()));
        if (card != null && card instanceof AstrayCard)
            ((AstrayCard) card).onStorageTriggered(triggers[0], triggers[1]);
        isDone = true;
    }
}