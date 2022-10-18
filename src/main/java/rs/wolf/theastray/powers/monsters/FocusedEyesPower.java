package rs.wolf.theastray.powers.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.monsters.BlueTheBoss;
import rs.wolf.theastray.utils.TAUtils;

public class FocusedEyesPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("FocusedEyesPower");
    private int baseCardsNeed;
    
    public FocusedEyesPower(BlueTheBoss owner, int cardsNeed, int invAmt) {
        super(ID, "mantra", PowerType.BUFF, owner);
        setValues(cardsNeed, invAmt);
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
                addToBot(new ApplyPowerAction(owner, owner, new InvinciblePower(owner, extraAmt)));
                addToBot(new QuickAction(() -> {
                    AbstractPower p = owner.getPower(InvinciblePower.POWER_ID);
                    if (p instanceof InvinciblePower) {
                        int maxAmt = ReflectionHacks.getPrivate(p, InvinciblePower.class, "maxAmt");
                        ReflectionHacks.setPrivate(p, InvinciblePower.class, "maxAmt", maxAmt + extraAmt);
                    }
                }));
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