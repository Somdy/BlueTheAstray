package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;

import java.util.Optional;

public class B19 extends AstrayProCard {
    public B19() {
        super(19, 1, CardTarget.NONE);
        setMagicValue(2, true);
        setExtraMagicValue(1, true);
//        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            for (int i = 0; i < magicNumber; i++) {
                Optional<AbstractCard> opt = LMSK.ReturnTrulyRndCardInCombat(c -> !isCardTypeOf(c, CardType.POWER) && c.color != CardColor.COLORLESS);
                opt.ifPresent(c -> addToTop(new MakeTempCardInHandAction(c, 1)));
            }
            for (int i = 0; i < getExtraMagic(); i++) {
                AbstractCard magic = CardMst.ReturnRndMagicInCombat(false);
                addToTop(new MakeTempCardInHandAction(magic, 1));
            }
        });
    }
    
//    void give() {
//        atbTmpAction(() -> {
//            for (int i = 0; i < magicNumber; i++) {
//                AbstractCard card = CardMst.ReturnRndMagicInCombat(this::isFreeMagicalCard);
//                addToTop(new MakeTempCardInHandAction(card, 1));
//            }
//        });
//    }
//    
//    void draw() {
//        if (cpr().drawPile.group.stream().noneMatch(this::isFreeMagicalCard)) {
//            addToBot(new TalkAction(cpr(), MSG[0], 1F, 1F));
//        } else {
//            addToBot(new DrawExptCardAction(magicNumber, this::isFreeMagicalCard).discardPileNotIncluded());
//        }
//    }
    
//    boolean isFreeMagicalCard(@NotNull AbstractCard c) {
//        return TAUtils.IsMagical(c) && c instanceof AstrayCard && (c.costForTurn == 0 || c.freeToPlay());
//    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeBaseCost(0);
//        branchingUpgrade();
    }
    
//    @Override
//    protected List<UpgradeBranch> branches() {
//        return new ArrayList<UpgradeBranch>() {{
//            add(() -> {
//                upgradeTexts();
//                setMagical(true);
//            });
//            add(() -> {
//                upgradeTexts(1);
//                setMagical(true);
//            });
//        }};
//    }
}
