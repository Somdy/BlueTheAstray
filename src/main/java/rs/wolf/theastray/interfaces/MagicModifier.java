package rs.wolf.theastray.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface MagicModifier {
    default int modifyValue(AbstractCard card) {
        return 0;
    }
    
    default int modifyValueLast(AbstractCard card) {
        return 0;
    }
}