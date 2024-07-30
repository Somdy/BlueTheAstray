package rs.wolf.theastray.cards.pros;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.exts.E89;
import rs.wolf.theastray.core.CardMst;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class B19 extends AstrayProCard {
    public B19() {
        super(19, 1, CardTarget.NONE);
        setMagicValue(1, true);
        setExtraMagicValue(1, true);
        setCanEnlighten(true);
        setMagical(true);
        cardsToPreview = new E89();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            List<AbstractCard> tmp = new ArrayList<>();
            if (!upgraded || finalBranch() == 0) {
                for (int i = 0; i < magicNumber; i++) {
                    Optional<AbstractCard> opt = LMSK.ReturnTrulyRndCardInCombat(c -> !isCardTypeOf(c, CardType.POWER) && c.color != CardColor.COLORLESS);
                    opt.ifPresent(c -> tmp.add(c.makeCopy()));
                }
            }
            for (int i = 0; i < getExtraMagic(); i++) {
                AbstractCard magic = CardMst.GetCard("星星");
                tmp.add(magic);
            }
            tmp.forEach(c -> {
                CardModifierManager.addModifier(c, new RetainMod());
                addToTop(new MakeTempCardInHandAction(c, 1));
            });
        });
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                upgradeTexts();
                upgradeBaseCost(0);
            });
            add(() -> {
                upgradeBaseCost(0);
                upgradeExtraMagic(1);
                setMagicValue(-99, true);
                setPromosValue(2, true);
                upgradeTexts(1);
            });
        }};
    }
}