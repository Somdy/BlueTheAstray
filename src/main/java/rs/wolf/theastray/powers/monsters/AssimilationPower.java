package rs.wolf.theastray.powers.monsters;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.monsters.BlueTheBoss;
import rs.wolf.theastray.utils.TAUtils;

public class AssimilationPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("AssimilationPower");
    private final int baseCardsNeed;
    
    public AssimilationPower(BlueTheBoss owner, int cardsNeed, int blockAmt) {
        super(ID, "focus", PowerType.BUFF, owner);
        setValues(cardsNeed, blockAmt);
        this.baseCardsNeed = cardsNeed;
        preloadString(s -> {
            setAmtValue(0, baseCardsNeed);
            setAmtValue(1, extraAmt);
        });
        updateDescription();
        isExtraAmtFixed = false;
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0) {
            amount--;
            if (amount <= 0) {
                flash();
                amount = baseCardsNeed;
                addToBot(new GainBlockAction(owner, owner, extraAmt));
            }
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return null;
    }
}