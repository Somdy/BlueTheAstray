package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class AshPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("AshPower");
    private static final List<AbstractCard> cardList = new ArrayList<>();
    
    public AshPower(int amount) {
        super(ID, "cExplosion", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AstrayCard && ((AstrayCard) card).isEnlightenCard() && !cardList.contains(card)) {
            if (card.upgraded) {
                int branch = ((AstrayCard) card).finalBranch();
                AstrayCard other = (AstrayCard) card.makeCopy();
                other.setChosenBranch(1 - branch);
                other.upgrade();
                addToBot(new MakeTempCardInHandAction(other, amount));
                cardList.add(other);
            }
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new AshPower(amount);
    }
}
