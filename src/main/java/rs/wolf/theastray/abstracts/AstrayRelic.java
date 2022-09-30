package rs.wolf.theastray.abstracts;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomRelic;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.data.RelicData;
import rs.wolf.theastray.powers.FrostPower;
import rs.wolf.theastray.utils.TAUtils;

public abstract class AstrayRelic extends LMCustomRelic {
    private AstrayRelic(@NotNull RelicData data) {
        super(data.getRelicID(), TAUtils.RelicImage(Integer.parseInt(data.getInternalID().substring(5))), 
                TAUtils.RelicOutlineImage(Integer.parseInt(data.getInternalID().substring(5))), data.getTier(), data.getSfx());
    }
    
    public AstrayRelic(int index) {
        this(DataMst.GetRelicData(index));
    }
    
    public AstrayRelic(String localname) {
        this(DataMst.GetRelicData(localname));
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    protected int frost(int originalValue, AbstractCreature target) {
        if (target instanceof AbstractMonster && ((AbstractMonster) target).getIntentBaseDmg() <= 0)
            originalValue *= 2;
        if (originalValue < 0) originalValue = 0;
        return originalValue;
    }
    
    protected FrostPower frostPower(AbstractCreature t, AbstractCreature s, int amount) {
        return new FrostPower(t, s, frost(amount, t));
    }
    
    protected ApplyPowerAction ApplyPower(AbstractCreature t, AbstractCreature s, AbstractPower p, int stackAmt) {
        return new ApplyPowerAction(t, s, p, stackAmt);
    }
    
    protected ApplyPowerAction ApplyPower(AbstractCreature t, AbstractCreature s, AbstractPower p) {
        return ApplyPower(t, s, p, p.amount);
    }
    
    protected void atbTmpAction(Runnable action) {
        addToBot(new QuickAction(action));
    }
}