package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class E82 extends AstrayExtCard {
    public E82() {
        super(82, 1, 14, CardTarget.NONE);
        setMagical(true);
        cardsToPreview = new E89();
        addTags(TACardEnums.ILLUSION);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            int size = calculateSize();
            if (size <= 0) return;
            if (upgraded) size *= magicNumber;
            for (int i = 0; i < size; i++) {
                addToTop(new NewQueueCardAction(cardsToPreview.makeCopy(), true, true, true));
            }
        });
    }
    
    private int calculateSize() {
        int size = 0;
        if (cpr().exhaustPile.isEmpty()) return size;
        if (!upgraded) {
            size = countSpecificCards(cpr().exhaustPile, c -> GlobalIDMst.CardID("星星").equals(c.cardID));
            cpr().exhaustPile.group.removeIf(c -> GlobalIDMst.CardID("星星").equals(c.cardID));
        } else {
            size = cpr().exhaustPile.size();
            cpr().exhaustPile.clear();
        }
        return size;
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}