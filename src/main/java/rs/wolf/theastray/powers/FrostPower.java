package rs.wolf.theastray.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.relics.Relic11;
import rs.wolf.theastray.utils.TAUtils;

public class FrostPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("FrostPower");
    
    public FrostPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(ID, "int", PowerType.DEBUFF, owner);
        setValues(source, amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public int getMaxAmount() {
        return Relic11.GetMaxAmount(super.getMaxAmount());
    }
    
    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && amount > 0) {
            damage -= amount;
            if (damage < 0) damage = 0;
        }
        return super.atDamageFinalGive(damage, type);
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        if (amount > 0) {
            addToBot(new ReducePowerAction(owner, owner, this, MathUtils.ceil(amount / 2F)));
        }
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        boolean useMagic = !(info instanceof CustomDmgInfo);
        if (info != null && info instanceof CustomDmgInfo && info.owner == source) {
            AbstractCard sourceCard = ((CustomDmgInfo) info).source.getCardFrom();
            if (sourceCard != null && TAUtils.IsMagical(sourceCard)) {
                useMagic = true;
            }
        }
        if (!useMagic)
            addToTop(new ReducePowerAction(owner, owner, this, 1));
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new FrostPower(owner, source, amount);
    }
}