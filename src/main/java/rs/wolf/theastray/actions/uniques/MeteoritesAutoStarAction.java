package rs.wolf.theastray.actions.uniques;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.core.CardMst;

public class MeteoritesAutoStarAction extends AstrayGameAction {
    private AbstractCard card;
    private boolean playing;
    
    public MeteoritesAutoStarAction(int amount) {
        this.amount = amount;
        playing = false;
        actionType = ActionType.SPECIAL;
    }
    
    private MeteoritesAutoStarAction(AbstractCard card, int slot) {
        this.card = card;
        this.amount = slot;
        playing = true;
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        isDone = true;
        if (amount <= 0 || playing && card == null) return;
        if (!playing) {
            if (cpr().hand.size() < BaseMod.MAX_HAND_SIZE) {
                amount--;
                if (amount > 0) {
                    addToTop(new MeteoritesAutoStarAction(amount));
                }
                addToTop(new MakeTempCardInHandAction(CardMst.GetCard("星星"), 1));
            } else {
                for (int i = amount; i > 0; i--) {
                    AbstractCard card = CardMst.GetCard("星星");
                    addToTop(new MeteoritesAutoStarAction(card, i));
                }
            }
        } else {
            card.current_y = scale(-200F);
            card.current_x = scale(-200F);
            card.target_x = Settings.WIDTH / 2F - scaleX(50F) * amount;
            card.target_y = Settings.HEIGHT / 2F;
            card.targetAngle = 0F;
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.applyPowers();
            LMSK.Player().limbo.group.add(card);
            addToTop(new NewQueueCardAction(card, true, false, true));
            addToTop(new UnlimboAction(card));
            addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }
    }
}