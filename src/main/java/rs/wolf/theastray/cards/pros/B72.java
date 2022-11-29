package rs.wolf.theastray.cards.pros;

import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.exts.B68;
import rs.wolf.theastray.cards.exts.E89;
import rs.wolf.theastray.core.CardMst;

import java.util.ArrayList;
import java.util.List;

public class B72 extends AstrayProCard {
    public B72() {
        super(72, 1, CardTarget.ALL_ENEMY);
        setMagicValue(3, true);
        setExtraMagicValue(4, true);
        setCanEnlighten(true);
        setMagical(true);
        MultiCardPreview.add(this, new E89());
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 1) {
            int handSize = cpr().hand.size();
            int left = BaseMod.MAX_HAND_SIZE - handSize;
            for (int i = 0; i < magicNumber; i++) {
                if (finalBranch() == 1 && left <= 0) {
                    addToTop(new NewQueueCardAction(CardMst.GetCard("星星"), true, true, true));
                } else {
                    addToTop(new MakeTempCardInHandAction(CardMst.GetCard("星星"), 1));
                }
                left--;
            }
        } else if (finalBranch() == 0) {
            atbTmpAction(() -> {
                AbstractCard[] cards = new AbstractCard[]{CardMst.GetCard("星星"), CardMst.GetCard("陨石召唤")};
                for (int i = 0; i < magicNumber; i++) {
                    int index =cardRandomRng().randomBoolean(getExtraMagic() / 100F) ? 1 : 0;
                    AbstractCard card = cards[index].makeCopy();
                    card.purgeOnUse = true;
                    addToTop(new NewQueueCardAction(card, true, true, true));
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
                upgradeMagicNumber(5);
                MultiCardPreview.clear(B72.this);
                cardsToPreview = CardMst.GetCard("星星");
            });
        }};
    }
}