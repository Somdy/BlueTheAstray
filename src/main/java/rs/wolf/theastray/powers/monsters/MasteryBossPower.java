package rs.wolf.theastray.powers.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.monsters.BlueTheBoss;
import rs.wolf.theastray.utils.TAUtils;

public class MasteryBossPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MasteryBossPower");
    private int baseCardsNeed;
    
    public MasteryBossPower(BlueTheBoss owner, int cardsNeed, int beatAmt) {
        super(ID, "establishment", PowerType.BUFF, owner);
        setValues(cardsNeed, beatAmt);
        this.baseCardsNeed = cardsNeed;
        preloadString(s -> {
            setAmtValue(0, amount);
            setAmtValue(1, extraAmt);
        });
        updateDescription();
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0) {
            amount--;
            if (amount <= 0) {
                flash();
                addToBot(new ApplyPowerAction(owner, owner, new BeatOfDeathPower(owner, extraAmt)));
                if (owner instanceof BlueTheBoss) {
                    addToBot(new ApplyPowerAction(owner, owner, new AssimilationPower((BlueTheBoss) owner, 1, 1)));
                }
                amount = baseCardsNeed;
            }
            updateDescription();
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return null;
    }
}
