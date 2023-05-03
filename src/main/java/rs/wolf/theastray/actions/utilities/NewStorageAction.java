package rs.wolf.theastray.actions.utilities;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.powers.StoragePower;
import rs.wolf.theastray.utils.GlobalManaMst;
import rs.wolf.theastray.utils.TAUtils;

public class NewStorageAction extends AstrayGameAction {
    public AbstractCard card;
    
    {
        amount = 0;
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        isDone = true;
        boolean noEnergy = EnergyPanel.getCurrentEnergy() <= 0;
        boolean noMana = !GlobalManaMst.HasMana();
        AbstractPower storage = StoragePower.MakeStorage(TAUtils.IsMagical(card));
        addToTop(new ApplyPowerAction(LMSK.Player(), LMSK.Player(), storage));
        if (noEnergy) {
            AbstractPower p = StoragePower.GetInstance(true);
            if (p instanceof StoragePower) {
                ((StoragePower) p).instant();
                amount++;
            }
        }
        if (noMana) {
            AbstractPower p = StoragePower.GetInstance(false);
            if (p instanceof StoragePower) {
                ((StoragePower) p).instant();
                amount++;
            }
        }
        if (amount >= 2) {
            addToTop(new MakeTempCardInHandAction(CardMst.GetCard("短路")));
        }
    }
}