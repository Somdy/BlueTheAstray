package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.utility.SimpleXCostActionBuilder;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;

public class B70 extends AstrayProCard {
    public B70() {
        super(70, -1, CardTarget.ALL);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleXCostActionBuilder(freeToPlayOnce, energyOnUse, upgraded)
                .addEffect((index, effect) -> effect)
                .addAction(effect -> {
                    if (upgraded) {
                        addToTop(new ModifyManaAction(effect));
                    }
                    for (int i = 0; i < effect - 1; i++) {
                        AbstractCard card = CardMst.ReturnRndMagicInCombat(c -> isCardTypeOf(c, CardType.ATTACK));
                        card.exhaustOnUseOnce = true;
                        addToTop(new NewQueueCardAction(card, true, true, true));
                    }
                }).build());
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}