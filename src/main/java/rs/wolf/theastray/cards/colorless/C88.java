package rs.wolf.theastray.cards.colorless;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayColorlessCard;
import rs.wolf.theastray.utils.TAUtils;

public class C88 extends AstrayColorlessCard {
    public C88() {
        super(88, -2, CardTarget.NONE);
        isEthereal = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void selfUpgrade() {}
    
    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (TAUtils.IsMagical(c)) {
            addToTop(new ExhaustSpecificCardAction(this, cpr().hand));
        }
    }
}