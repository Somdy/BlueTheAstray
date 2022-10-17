package rs.wolf.theastray.cards.exts;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.pros.B68;
import rs.wolf.theastray.core.CardMst;

import java.util.ArrayList;
import java.util.List;

public class B72 extends AstrayExtCard {
    public B72() {
        super(72, 1, 12, CardTarget.ALL_ENEMY);
        setMagicValue(2, true);
        setExtraMagicValue(4, true);
        setCanEnlighten(true);
        setMagical(true);
        MultiCardPreview.add(this, new E89(), new B68());
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            AbstractCard[] cards = new AbstractCard[]{CardMst.GetCard("星星"), CardMst.GetCard("陨石召唤")};
            for (int i = 0; i < magicNumber; i++) {
                int index = 0;
                if ((!upgraded || finalBranch() == 0) && cardRandomRng().randomBoolean(getExtraMagic() / 100F)) {
                    index = 1;
                }
                AbstractCard card = cards[index].makeCopy();
                card.purgeOnUse = true;
                addToTop(new NewQueueCardAction(card, true, true, true));
            }
        });
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
                upgradeExtraMagic(12);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(3);
                MultiCardPreview.clear(B72.this);
                cardsToPreview = CardMst.GetCard("星星");
            });
        }};
    }
}