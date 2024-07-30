package rs.wolf.theastray.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.utils.LMDamageInfoHelper;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.relics.Relic11;
import rs.wolf.theastray.utils.TAUtils;

public class BurntPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("BurntPower");
    
    public BurntPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(ID, "burnt", PowerType.DEBUFF, owner);
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
    public int getMaxAmount() {
        return Relic11.GetMaxAmount(super.getMaxAmount());
    }
    
    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (amount > 0) {
//            addToBot(new LoseHPAction(owner, source, amount, AbstractGameAction.AttackEffect.FIRE));
            addToBot(new DamageAction(owner, LMDamageInfoHelper.Create(source, amount, 
                    DamageInfo.DamageType.HP_LOSS, TAUtils.MAGICAL_DAMAGE), TACardEnums.ASH_EXPLOSION));
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
        boolean useMagic = TAUtils.IsMagicalDamage(info);
//        if (info != null) {
//            
//            if (info instanceof CustomDmgInfo && info.owner == source) {
//                AbstractCard sourceCard = ((CustomDmgInfo) info).source.getCardFrom();
//                if (sourceCard != null && TAUtils.IsMagical(sourceCard)) {
//                    useMagic = true;
//                }
//            } else {
//                useMagic = LMDamageInfoHelper.HasTag(info, TAUtils.MAGICAL_DAMAGE);
//            }
//        }
        if (useMagic) {
            checkValues();
            addToTop(new ApplyPowerAction(owner, owner, this, extraAmt));
        }
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new BurntPower(owner, source, amount);
    }
}