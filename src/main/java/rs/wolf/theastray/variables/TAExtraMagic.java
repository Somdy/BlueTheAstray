package rs.wolf.theastray.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.utils.TAUtils;

public class TAExtraMagic extends DynamicVariable {
    public static final String KEY = TAUtils.MakeID("extras");
    
    @Override
    public String key() {
        return KEY;
    }
    
    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AstrayCard)
            return ((AstrayCard) card).isExtraMagicModified();
        return false;
    }
    
    @Override
    public int value(AbstractCard card) {
        if (card instanceof AstrayCard)
            return ((AstrayCard) card).getExtraMagic();
        return 0;
    }
    
    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AstrayCard)
            return ((AstrayCard) card).getBaseExtraMagic();
        return 0;
    }
    
    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AstrayCard)
            return ((AstrayCard) card).isUpgradedExtraMagic();
        return false;
    }
}