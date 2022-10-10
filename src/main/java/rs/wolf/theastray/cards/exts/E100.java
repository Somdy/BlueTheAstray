package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.cards.pros.B3;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.utils.GlobalManaMst;

public class E100 extends AstrayExtCard {
    public E100() {
        super(100, 2, 9, CardTarget.ALL);
        setMagicValue(1, true);
        setMagical(true);
        cardsToPreview = new B3();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        int currentMana = GlobalManaMst.CurrentMana();
        addToBot(new ModifyManaAction(-currentMana));
        if (currentMana > 0) {
            atbTmpAction(() -> {
                for (int i = 0; i < currentMana; i++) {
                    AstrayCard card = (AstrayCard) CardMst.GetCard(3);
                    card.purgeOnUse = true;
                    if (upgraded) {
                        int branch = cardRandomRng().random(0, 1);
                        card.setChosenBranch(branch);
                        card.upgrade();
                    }
                    for (int j = 0; j < magicNumber; j++) {
                        addToTop(new NewQueueCardAction(card, true, true, true));
                    }
                }
            });
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        if (cardsToPreview != null)
            cardsToPreview.upgrade();
    }
}