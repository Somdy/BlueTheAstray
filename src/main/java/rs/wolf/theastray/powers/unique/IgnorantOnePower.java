package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class IgnorantOnePower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("IgnorantOnePower");
    
    public IgnorantOnePower(int amount) {
        super(ID, "ignorance", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0) {
            flash();
            addToBot(new QuickAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs()) {
                    addToTop(new ApplyPowerAction(m, owner, burntPower(m, owner, amount)));
                }
            }));
        }
    }
    
    @Override
    public void onInitialApplication() {
        addToTop(new QuickAction(() -> {
            if (cpr().powers.stream().anyMatch(p -> p instanceof IgnorancePower)) {
                cpr().powers.stream().filter(p -> p instanceof IgnorancePower)
                        .findFirst()
                        .ifPresent(p -> addToTop(new RemoveSpecificPowerAction(owner, owner, p)));
            }
        }));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new IgnorantOnePower(amount);
    }
}