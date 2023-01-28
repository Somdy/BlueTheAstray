package rs.wolf.theastray.cards.pros;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.actions.uniques.MeteoritesAutoStarAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.exts.E89;
import rs.wolf.theastray.core.CardMst;

import java.util.ArrayList;
import java.util.List;

public class B72 extends AstrayProCard {
    public B72() {
        super(72, 1, CardTarget.ALL_ENEMY);
        setMagicValue(3, true);
        setExtraMagicValue(6, true);
        setCanEnlighten(true);
        setMagical(true);
        MultiCardPreview.add(this, new E89());
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            addToBot(new MakeTempCardInHandAction(CardMst.GetCard("星星"), magicNumber));
        } else if (finalBranch() == 1) {
            addToBot(new MeteoritesAutoStarAction(magicNumber));
        } else if (finalBranch() == 0) {
            atbTmpAction(() -> {
                AbstractCard[] cards = new AbstractCard[]{CardMst.GetCard("星星"), CardMst.GetCard("陨石召唤")};
                for (int i = 0; i < magicNumber; i++) {
                    int index =cardRandomRng().randomBoolean(getExtraMagic() / 100F) ? 1 : 0;
                    AbstractCard card = cards[index].makeCopy();
                    card.purgeOnUse = true;
                    card.applyPowers();
                    addToTop(new NewQueueCardAction(card, true, false, true));
                }
            });
        }
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>(){{
            add(() -> {
                upgradeTexts();
                MultiCardPreview.add(B72.this, CardMst.GetCard("陨石召唤"));
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(2);
                MultiCardPreview.clear(B72.this);
                cardsToPreview = CardMst.GetCard("星星");
            });
        }};
    }
}