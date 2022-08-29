package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;

import java.util.ArrayList;
import java.util.List;

public class B32 extends AstrayProCard {
    public B32() {
        super(32, 1, CardTarget.NONE);
        setMagicValue(2, true);
        setExtraMagicValue(1, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            for (int i = 0; i < magicNumber; i++) {
                AbstractCard card = CardMst.ReturnRndCardInCombat(c -> c.hasTag(TACardEnums.ILLUSION));
                addToBot(new MakeTempCardInHandAction(card, 1));
            }
            addToBot(new MakeTempCardInDiscardAction(CardMst.GetCard("镜中幻像"), 1));
        } else if (finalBranch() == 1) {
            for (int i = 0; i < magicNumber; i++) {
                AbstractCard card = CardMst.ReturnRndCardInCombat(c -> c.hasTag(TACardEnums.ILLUSION));
                addToBot(new MakeTempCardInDiscardAction(card, 1));
            }
            addToBot(new DrawCardAction(s, getExtraMagic()));
        }
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
                upgradeMagicNumber(1);
            });
            add(() -> {
                upgradeTexts(1);
                exhaust = true;
            });
        }};
    }
}