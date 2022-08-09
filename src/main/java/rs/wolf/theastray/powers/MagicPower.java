package rs.wolf.theastray.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.interfaces.MagicModifier;
import rs.wolf.theastray.utils.TAUtils;

public class MagicPower extends AstrayPower implements MagicModifier {
    public static final String ID = TAUtils.MakeID("MagicPower");
    
    public MagicPower(AbstractCreature owner, int amount) {
        super(ID, "focus", PowerType.BUFF, owner);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public int modifyValue(AbstractCard card) {
        int base = 0;
        if (card instanceof AstrayCard) {
            base += amount;
            if (((AstrayCard) card).getPromos() > 0)
                base += ((AstrayCard) card).getPromos();
        }
        return base;
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MagicPower(owner, amount);
    }
}