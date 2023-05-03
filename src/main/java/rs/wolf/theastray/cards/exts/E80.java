package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.BlurPower;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.GlobalManaMst;

public class E80 extends AstrayExtCard {
    public E80() {
        super(80, 2, 14, CardTarget.SELF);
        setBlockValue(7, true);
        setMagicValue(1, true);
        setMagicalDerivative(true);
        setStorage(true);
        exhaust = true;
        addTip(MSG[0], MSG[1]);
        addTags(TACardEnums.ILLUSION);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new BlurPower(s, magicNumber)));
        attTmpAction(() -> {
            if (!cpr().discardPile.isEmpty()) {
                for (AbstractCard card : cpr().discardPile.group) {
                    addToTop(new ExhaustSpecificCardAction(card, cpr().discardPile));
                }
            }
            int totalMana = GlobalManaMst.CurrentMana();
            if (totalMana > 0) {
                int blocks = totalMana * block;
                addToTop(new GainBlockAction(s, blocks));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(1);
    }
}