package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.interfaces.cards.RUM;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class MindBlossomPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MindBlossomPower");
    
    public MindBlossomPower() {
        super(ID, "panache", PowerType.BUFF, AbstractDungeon.player);
        stackable = false;
        updateDescription();
    }
    
    @Override
    public void onMakingCardInCombat(AbstractCard card, CardGroup destination) {
        if (card instanceof AstrayCard && ((AstrayCard) card).isEnlightenCard() && !card.upgraded) {
            flash();
            int branch = ((AstrayCard) card).getBranchForRandomUpgrading(RUM.MASTER_REALITY);
            ((AstrayCard) card).setChosenBranch(branch);
            card.upgrade();
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MindBlossomPower();
    }
}
