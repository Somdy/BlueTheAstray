package rs.wolf.theastray.powers.unique;

import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.RUM;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.utils.TAUtils;

public class MindBlossomEmptinessPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("MindBlossomEmptinessPower");
    
    public MindBlossomEmptinessPower() {
        super(ID, "mindemptiness", PowerType.BUFF, AbstractDungeon.player);
        stackable = false;
        updateDescription();
    }
    
    @Override
    public void onMakingCardInCombat(AbstractCard card, CardGroup destination) {
        CardModifierManager.addModifier(card, new EtherealMod());
    }
    
    @Override
    public void onInitialApplication() {
        cpr().powers.stream().filter(p -> p instanceof MindBlossomPower)
                .forEach(p -> addToTop(new RemoveSpecificPowerAction(cpr(), cpr(), p)));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MindBlossomEmptinessPower();
    }
}