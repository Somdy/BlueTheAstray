package rs.wolf.theastray.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface MagicModifier {
    default int modifyMagicValue(AbstractCard card) {
        return 0;
    }
    default int modifyDamageValue(AbstractCard card) {
        return modifyMagicValue(card);
    }
    default int modifyBlockValue(AbstractCard card) {
        return modifyMagicValue(card);
    }
}