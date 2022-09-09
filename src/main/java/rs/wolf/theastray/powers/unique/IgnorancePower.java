package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class IgnorancePower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("IgnorancePower");
    
    public IgnorancePower(int amount) {
        super(ID, "regrow", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (isCardTypeOf(card, AbstractCard.CardType.ATTACK) && amount > 0) {
            flash();
            addToBot(new QuickAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs()) {
                    addToTop(new ApplyPowerAction(m, owner, burntPower(m, owner, amount)));
                }
            }));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new IgnorancePower(amount);
    }
}