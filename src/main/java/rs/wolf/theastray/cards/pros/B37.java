package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.colorless.C87;
import rs.wolf.theastray.core.CardMst;

import java.util.ArrayList;
import java.util.List;

public class B37 extends AstrayProCard {
    public B37() {
        super(37, 0, CardTarget.SELF);
        setMagicValue(3, true);
        setExtraMagicValue(2, true);
        setCanEnlighten(true);
        cardsToPreview = new C87();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainEnergyAction(magicNumber));
        if (!upgraded || finalBranch() == 0) {
            addToBot(new ModifyManaAction(-getExtraMagic()));
        } else if (finalBranch() == 1) {
            addToBot(new MakeTempCardInHandAction(CardMst.GetCard("魔网断开"), getExtraMagic()));
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
                upgradeExtraMagic(-1);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeExtraMagic(-1);
            });
        }};
    }
}