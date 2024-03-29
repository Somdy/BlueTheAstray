package rs.wolf.theastray.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.interfaces.MagicModifier;
import rs.wolf.theastray.powers.unique.AntiMagicPower;
import rs.wolf.theastray.relics.Relic11;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.Optional;

public final class MagicPower extends AstrayPower implements MagicModifier {
    public static final String ID = TAUtils.MakeID("MagicPower");
    
    public MagicPower(AbstractCreature owner, int amount) {
        super(ID, "magic", PowerType.BUFF, owner);
        canGoNegative = true;
        setValues(amount);
        preloadString(s -> {
            s[0] = this.amount > 0 ? DESCRIPTIONS[0] : DESCRIPTIONS[1];
//            if (hasC88InHand()) {
//                cpr().hand.group.stream()
//                        .filter(c -> c.cardID.equals(GlobalIDMst.CardID("短路")))
//                        .findFirst()
//                        .ifPresent(c -> s[0] = String.format(DESCRIPTIONS[2], c.name));
//            }
            setAmtValue(0, Math.abs(this.amount));
        });
        updateDescription();
        type = amount > 0 ? PowerType.BUFF : PowerType.DEBUFF;
    }
    
    boolean hasC88InHand() {
        return cpr().hand.group.stream().anyMatch(c -> c.cardID.equals(GlobalIDMst.CardID("短路")));
    }
    
    @Override
    public int getMaxAmount() {
        return Relic11.GetMaxAmount(super.getMaxAmount());
    }
    
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount == 0) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        type = amount > 0 ? PowerType.BUFF : PowerType.DEBUFF;
    }
    
    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount == 0) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        type = amount > 0 ? PowerType.BUFF : PowerType.DEBUFF;
    }
    
    @Override
    public int modifyDamageValue(AbstractCard card) {
        int base = 0;
        if (card instanceof AstrayCard && !hasC88InHand()) {
            base += amount;
            if (((AstrayCard) card).getPromos() > 0) {
                int promos = ((AstrayCard) card).getPromos();
                base += base * promos;
            }
        }
        return base;
    }
    
    @Override
    public int modifyBlockValue(AbstractCard card) {
        int base = 0;
        if (card instanceof AstrayCard && !hasC88InHand()) {
            base += amount;
            if (((AstrayCard) card).getPromos() > 0) {
                int promos = ((AstrayCard) card).getPromos();
                base += base * promos;
            }
        }
        return base;
    }
    
    @Override
    public int modifyMagicValue(AbstractCard card) {
        int base = 0;
        if (card instanceof AstrayCard && !hasC88InHand()) {
            base += (amount / 2);
            if (((AstrayCard) card).getPromos() > 0) {
                int promos = ((AstrayCard) card).getPromos();
                base += base * promos;
            }
        }
        return base;
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MagicPower(owner, amount);
    }
}