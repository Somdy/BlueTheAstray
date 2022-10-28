package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.RUM;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.actions.commons.ChooseBranchForEnlightenCardAction;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.utils.TAUtils;

public class MindBlossomMasteryPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MindBlossomMasteryPower");
    
    public MindBlossomMasteryPower() {
        super(ID, "panache", PowerType.BUFF, AbstractDungeon.player);
        stackable = false;
        updateDescription();
    }
    
    @Override
    public void onMakingCardInCombat(AbstractCard card, CardGroup destination) {
        if (!card.upgraded && (TAUtils.IsMagical(card) || card instanceof AstrayExtCard)) {
            if (card instanceof BranchableUpgradeCard && ((BranchableUpgradeCard) card).canBranch()) {
                if (card instanceof AstrayCard)
                    ((AstrayCard) card).setFakeRestroom(true);
                addToTop(new ChooseBranchForEnlightenCardAction(card, String.format(DESCRIPTIONS[1], card.name)));
            } else {
                card.upgrade();
            }
        }
    }
    
    @Override
    public void onInitialApplication() {
        cpr().powers.stream().filter(p -> p instanceof MindBlossomPower)
                .forEach(p -> addToTop(new RemoveSpecificPowerAction(cpr(), cpr(), p)));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MindBlossomMasteryPower();
    }
}