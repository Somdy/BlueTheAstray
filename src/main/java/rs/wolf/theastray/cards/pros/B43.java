package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.colorless.C87;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.powers.unique.StoneFleshPower;

public class B43 extends AstrayProCard {
    public B43() {
        super(43, 1, CardTarget.SELF);
        setMagicValue(25, true);
        setMagical(true);
        exhaust = true;
        cardsToPreview = new C87();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new StoneFleshPower(s, (magicNumber / 100F))));
        addToBot(new MakeTempCardInHandAction(CardMst.GetCard("魔网断开"), 1));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        exhaust = false;
    }
}