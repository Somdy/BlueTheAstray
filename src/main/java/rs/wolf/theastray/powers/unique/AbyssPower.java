package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.actions.common.StackPowerAmountAction;
import rs.lazymankits.listeners.TurnEventListener;
import rs.lazymankits.listeners.tools.CreatureStatus;
import rs.lazymankits.listeners.tools.TurnStatus;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.TAUtils;

public class AbyssPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("AbyssPower");
    
    public AbyssPower(int amount) {
        super(ID, "nightmare", PowerType.BUFF, AbstractDungeon.player);
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void atStartOfTurnPostDraw() {
        TurnStatus status = TurnEventListener.GetTurnEndingStatus(TurnEventListener.LastTurn[0]);
        if (status != null) {
            flash();
            for (AbstractMonster m : getAllLivingMstrs()) {
                CreatureStatus cs = status.get(m);
                if (cs != null && !cs.powers.isEmpty() && cs.powers.stream().anyMatch(p -> isPowerTypeOf(p, PowerType.DEBUFF))) {
                    for (AbstractPower p : cs.powers) {
                        if (isPowerTypeOf(p, PowerType.DEBUFF)) {
                            if (m.hasPower(p.ID)) {
                                stackPowerAmount(m, p);
                            } else {
                                applyNewPower(m, p);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void applyNewPower(AbstractCreature t, @NotNull AbstractPower p) {
        p.amount = Integer.compare(p.amount, 0);
        p.amount *= this.amount;
        p.updateDescription();
        addToTop(new ApplyPowerAction(t, owner, p));
    }
    
    private void stackPowerAmount(AbstractCreature t, @NotNull AbstractPower p) {
        int stack = amount;
        if (p.amount < 0) stack *= -1;
        addToTop(new StackPowerAmountAction(t, p, stack));
    }
    
    private boolean isDebuff(AbstractPower p) {
        return isPowerTypeOf(p, PowerType.DEBUFF) && !p.ID.equals(GainStrengthPower.POWER_ID);
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new AbyssPower(amount);
    }
}
