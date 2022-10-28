package rs.wolf.theastray.actions.commons;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.wolf.theastray.abstracts.AstrayGameAction;

public class ChooseBranchForEnlightenCardAction extends AstrayGameAction {
    private AbstractCard card;
    private String msg;
    
    public ChooseBranchForEnlightenCardAction(AbstractCard card, String msg) {
        this.card = card;
        this.msg = msg;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        if (card.upgraded || !(card instanceof BranchableUpgradeCard)
                || !((BranchableUpgradeCard) card).canBranch()) {
            isDone = true;
            return;
        }
        if (duration == startDuration) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            tmp.addToBottom(card);
            AbstractDungeon.gridSelectScreen.open(tmp, 1, msg, true, false, false, false);
            ReflectionHacks.setPrivate(AbstractDungeon.gridSelectScreen, GridCardSelectScreen.class, "hoveredCard", this.card);
            tickDuration();
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                card.upgrade();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            isDone = true;
        }
    }
}