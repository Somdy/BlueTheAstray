package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class SkiingPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("SkiingPower");
    
    public SkiingPower(int turns, int debuffs) {
        super(ID, "vigor", PowerType.BUFF, AbstractDungeon.player);
        setValues(turns, debuffs);
        preloadString(s -> {
            setAmtValue(0, extraAmt);
            setAmtValue(1, amount);
        });
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new QuickAction(() -> {
            addToTop(new ReducePowerAction(owner, owner, this, 1));
            for (AbstractMonster m : getAllLivingMstrs()) {
                addToTop(new ApplyPowerAction(m, owner, frostPower(m, owner, extraAmt)));
            }
        }));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new SkiingPower(amount, extraAmt);
    }
}