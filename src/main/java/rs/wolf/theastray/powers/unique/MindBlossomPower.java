package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.RUM;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.utils.TAUtils;

public class MindBlossomPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MindBlossomPower");
    
    public MindBlossomPower() {
        super(ID, "mindblossom", PowerType.BUFF, AbstractDungeon.player);
        stackable = false;
        updateDescription();
    }
    
    @Override
    public void onMakingCardInCombat(AbstractCard card, CardGroup destination) {
        if (!card.upgraded && (TAUtils.IsMagical(card) || card instanceof AstrayExtCard)) {
            if (card instanceof BranchableUpgradeCard && ((BranchableUpgradeCard) card).canBranch()) {
                int index = ((BranchableUpgradeCard) card).getBranchForRandomUpgrading(RUM.MASTER_REALITY);
                ((BranchableUpgradeCard) card).setChosenBranch(index);
            }
            card.upgrade();
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MindBlossomPower();
    }
}