package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayExtCard;

import java.util.ArrayList;
import java.util.List;

public class E82 extends AstrayExtCard {
    public E82() {
        super(82, 1, 14, CardTarget.NONE);
        setMagical(true);
        setCanEnlighten(true);
        exhaust = true;
        cardsToPreview = new E89();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int size = cpr().exhaustPile.size();
            if (size <= 0) return;
            cpr().exhaustPile.clear();
            if (!upgraded || finalBranch() == 0) {
                AbstractCard card = cardsToPreview.makeCopy();
                if (upgraded) card.upgrade();
                addToTop(new MakeTempCardInHandAction(card, size));
            } else if (finalBranch() == 1) {
                for (int i = 0; i < size; i++) {
                    addToTop(new NewQueueCardAction(cardsToPreview.makeCopy(), true, 
                            true, true));
                }
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> upgradeTexts());
            add(() -> upgradeTexts(1));
        }};
    }
}