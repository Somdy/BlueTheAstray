package rs.wolf.theastray.actions.utilities;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.colorless.C88;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.utils.GlobalManaMst;

import java.util.Arrays;

public class TriggerStorageAction extends AstrayGameAction {
    private boolean[] triggers;
    private AbstractCard card;
    
    public TriggerStorageAction(boolean energy, boolean mana, AbstractCard card) {
        triggers = new boolean[] {energy, mana, energy && mana, !energy && !mana};
        this.card = card;
        Leader.devLog("storage result: " + Arrays.toString(triggers));
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        if (triggers[3]) {
            if (card != null && card instanceof AstrayCard)
                ((AstrayCard) card).onNeitherStorageTriggers();
            Leader.devLog("no storage");
            isDone = true;
            return;
        }
        Leader.devLog("storage before: [energy] {" + EnergyPanel.getCurrentEnergy() + "}" +
                " [mana] {" + GlobalManaMst.CurrentMana() + "}");
        if (triggers[0])
            addToTop(new GainEnergyAction(1));
        if (triggers[1])
            addToTop(new ModifyManaAction(1));
        if (triggers[2])
            addToTop(new MakeTempCardInHandAction(CardMst.GetCard("短路")));
        if (card != null && card instanceof AstrayCard)
            ((AstrayCard) card).onStorageTriggered(triggers[0], triggers[1]);
        isDone = true;
    }
}