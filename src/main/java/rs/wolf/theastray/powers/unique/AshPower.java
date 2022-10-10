package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AshPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("AshPower");
    private static final List<UUID> cardList = new ArrayList<>();
    
    public AshPower(int amount) {
        super(ID, "cExplosion", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AstrayCard && ((AstrayCard) card).isEnlightenCard() && !cardList.contains(card.uuid)) {
            if (card.upgraded) {
                int branch = ((AstrayCard) card).finalBranch();
                AstrayCard other = (AstrayCard) card.makeCopy();
                other.setChosenBranch(1 - branch);
                other.upgrade();
                Leader.devLog("RETURNING OTHER BRANCH");
                for (int i = 0; i < amount; i++) {
                    addToBot(new MakeTempCardInHandAction(other, true, true));
                }
                Leader.devLog("branch [" + other.uuid + "] of [" + card.uuid + "] returned to hand");
                cardList.add(other.uuid);
            }
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new AshPower(amount);
    }
}
