package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.RUM;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class AmendmentPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("AmendmentPower");
    private static final List<AbstractCard> cardList = new ArrayList<>();
    
    public AmendmentPower() {
        super(ID, "master_reality", PowerType.BUFF, AbstractDungeon.player);
        stackable = false;
        updateDescription();
    }
    
    public static boolean BackToDrawPile(AbstractCard card) {
        return cardList.remove(card);
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!card.upgraded && !cardList.contains(card)) {
            if (card.canUpgrade()) {
                if (card instanceof BranchableUpgradeCard && ((BranchableUpgradeCard) card).canBranch()) {
                    int branch = ((BranchableUpgradeCard) card).getBranchForRandomUpgrading(RUM.ARMAMENTS);
                    ((BranchableUpgradeCard) card).setChosenBranch(branch);
                }
                card.upgrade();
                card.applyPowers();
            }
            cardList.add(card);
        }
    }
    
    @Override
    public void onInitialApplication() {
        cardList.clear();
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new AmendmentPower();
    }
}