package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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

public class MeteoritePower extends AstrayPower {
    public static final String PID = TAUtils.MakeID("MeteoritePower");
    private static int ID_INDEX = 0;
    private boolean justApplied;
    
    public MeteoritePower(int damage, int powers) {
        super(PID, "vigor", PowerType.BUFF, AbstractDungeon.player);
        setValues(damage, powers);
        autoID();
        justApplied = true;
        preloadString(s -> {
            setAmtValue(0, amount);
            setAmtValue(1, extraAmt);
            if (justApplied)
                s[0] = DESCRIPTIONS[0];
            else 
                s[0] = DESCRIPTIONS[1];
        });
        updateDescription();
    }
    
    private void autoID() {
        this.ID = PID + ID_INDEX;
        ID_INDEX++;
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (justApplied) {
            justApplied = false;
            updateDescription();
            return;
        }
        addToBot(new QuickAction(() -> {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            for (AbstractMonster m : getAllLivingMstrs()) {
                addToTop(new ApplyPowerAction(m, owner, burntPower(m, owner, extraAmt)));
            }
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null) {
                addToTop(new NullableSrcDamageAction(m, crtDmgInfo(owner, amount, DamageInfo.DamageType.THORNS), 
                        AbstractGameAction.AttackEffect.NONE));
                addToTop(new VFXAction(new VerticalImpactEffect(m.hb.cX, m.hb.cY)));
            }
        }));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MeteoritePower(amount, extraAmt);
    }
}