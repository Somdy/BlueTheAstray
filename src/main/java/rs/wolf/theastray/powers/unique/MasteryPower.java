package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class MasteryPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MasteryPower");
    
    public MasteryPower(int amount) {
        super(ID, "establishment", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void onMakingCardInCombat(AbstractCard card, CardGroup destination) {
        if (amount > 0) {
            flashWithoutSound();
            card.modifyCostForCombat(-amount);
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MasteryPower(amount);
    }
}
