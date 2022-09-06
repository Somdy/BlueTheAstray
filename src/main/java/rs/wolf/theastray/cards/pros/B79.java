package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;

public class B79 extends AstrayProCard {
    public B79() {
        super(79, 1, CardTarget.ALL_ENEMY);
        setDamageValue(14, true);
        setMagicValue(1, true);
        setMagical(true);
        setStorage(true);
        addTip(ILLUSION_CARD);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAllEnemiesAction(s, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, m -> {
            AbstractCard card = CardMst.ReturnRndCardInCombat(c -> c.hasTag(TACardEnums.ILLUSION)
                    && !isCardTypeOf(c, CardType.CURSE), true);
            if (!upgraded) {
                addToTop(new MakeTempCardInDrawPileAction(card, magicNumber, true, true));
            } else {
                addToTop(new MakeTempCardInHandAction(card, magicNumber));
            }
        }));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}