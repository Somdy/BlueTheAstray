package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class FrostShieldPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MagicalBodyPower");
    private final AbstractCard card;
    
    public FrostShieldPower(AbstractCreature owner, AbstractCard card, int amount) {
        super(ID, "carddraw", PowerType.BUFF, owner);
        setValues(amount);
        this.card = card;
        preloadString(s -> s[0] = String.format(s[0], this.card.name));
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        if (amount > 0 && card != null) {
            flash();
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToTop(new MakeTempCardInHandAction(card.makeCopy(), amount));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new FrostShieldPower(owner, card, amount);
    }
}
