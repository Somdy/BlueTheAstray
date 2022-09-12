package rs.wolf.theastray.cards.colorless;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayColorlessCard;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.utils.TAUtils;

public class C88 extends AstrayColorlessCard {
    private static boolean UpdatedPower = false;
    
    public C88() {
        super(88, -2, CardTarget.NONE);
    }
    
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!UpdatedPower && p.hasPower(MagicPower.ID)) {
            p.getPower(MagicPower.ID).updateDescription();
            UpdatedPower = true;
        }
        return false;
    }
    
    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (TAUtils.IsMagical(c) && inHand()) {
            addToTop(new ExhaustSpecificCardAction(this, cpr().hand));
            if (UpdatedPower && cpr().hasPower(MagicPower.ID)) {
                cpr().getPower(MagicPower.ID).updateDescription();
                UpdatedPower = false;
            }
        }
    }
    
    @Override
    public void onMoveToDiscard() {
        if (cpr().hasPower(MagicPower.ID)) {
            cpr().getPower(MagicPower.ID).updateDescription();
        }
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void selfUpgrade() {}
}