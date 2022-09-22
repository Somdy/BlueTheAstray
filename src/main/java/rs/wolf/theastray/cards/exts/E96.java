package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import rs.lazymankits.actions.common.StackPowerAmountAction;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E96 extends AstrayExtCard {
    public E96() {
        super(96, 3, 4, CardTarget.SELF);
        setMagicValue(3, true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> s.powers.stream().filter(p -> p instanceof MetallicizePower)
                .findFirst()
                .ifPresent(p -> addToTop(new StackPowerAmountAction(s, p, p.amount))));
        atbTmpAction(() -> s.powers.stream().filter(p -> p instanceof PlatedArmorPower)
                .findFirst()
                .ifPresent(p -> {
                    int amount = p.amount;
                    addToTop(ApplyPower(s, s, new MetallicizePower(s, amount)));
                    addToTop(new RemoveSpecificPowerAction(s, s, p));
                }));
        addToBot(ApplyPower(s, s, new PlatedArmorPower(s, magicNumber)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(2);
    }
}
