package rs.wolf.theastray.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

public class BurntPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("BurntPower");
    
    public BurntPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(ID, "combust", PowerType.DEBUFF, owner);
        setValues(source, amount);
        preloadString((s) -> checkValues());
        updateDescription();
    }
    
    private void checkValues() {
        setAmtValue(0, amount);
        extraAmt = 1;
        if (owner instanceof AbstractMonster && ((AbstractMonster) owner).getIntentBaseDmg() > 0) {
            extraAmt *= 2;
        }
        setAmtValue(1, extraAmt);
    }
    
    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (amount > 0) {
            addToBot(new LoseHPAction(owner, source, amount, AbstractGameAction.AttackEffect.FIRE));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        updateDescription();
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info != null && info instanceof CustomDmgInfo && info.owner == source) {
            AbstractCard sourceCard = ((CustomDmgInfo) info).source.getCardFrom();
            if (sourceCard != null && TAUtils.IsMagical(sourceCard)) {
                checkValues();
                addToTop(new ApplyPowerAction(owner, owner, this, extraAmt));
            }
        }
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new BurntPower(owner, source, amount);
    }
}