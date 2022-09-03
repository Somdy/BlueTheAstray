package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B47 extends AstrayProCard {
    public B47() {
        super(47, 2, CardTarget.SELF);
        setMagicValue(4, true);
        setBlockValue(6, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!cpr().hand.isEmpty()) {
            atbTmpAction(() -> {
                List<AbstractCard> tmp = new ArrayList<>(cpr().hand.group);
                int size = Math.min(tmp.size(), magicNumber);
                for (int i = 0; i < size; i++) {
                    AbstractCard c = tmp.remove(cardRandomRng().random(tmp.size() - 1));
                    addToTop(new ExhaustSpecificCardAction(c, cpr().hand));
                    if (tmp.isEmpty()) break;
                }
                addToTop(new GainBlockAction(s, size * block));
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
                upgradeBlock(3);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(-1);
            });
        }};
    }
}