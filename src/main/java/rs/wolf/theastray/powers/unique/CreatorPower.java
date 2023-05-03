package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class CreatorPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("CreatorPower");
    
    public CreatorPower(int blocks) {
        super(ID, "creation", PowerType.BUFF, AbstractDungeon.player);
        setValues(blocks);
        preloadString(s -> setAmtValue(0, amount));
        updateDescription();
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0 && cpr().masterDeck.group.stream().noneMatch(c -> c.uuid == card.uuid)) {
            flash();
            addToBot(new GainBlockAction(cpr(), amount, true));
        }
    }
    
    @Override
    public void onMakingCardInCombat(AbstractCard card, CardGroup destination) {
        if (amount > 0 && cpr().masterDeck.group.stream().noneMatch(c -> c.uuid == card.uuid)) {
            flash();
            addToBot(new GainBlockAction(cpr(), amount, true));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new CreatorPower(amount);
    }
}
