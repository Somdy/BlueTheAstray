package rs.wolf.theastray.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.utils.TAUtils;

public class StoragePower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("StoragePower");
    private final boolean energy;
    
    private StoragePower(AbstractCreature owner, int amount, boolean energy) {
        super(ID, "magic", PowerType.BUFF, owner);
        setDefaults(ID + (energy ? "ENERGY" : "MANA"), DESCRIPTIONS[energy ? 1 : 0], PowerType.BUFF);
        setValues(amount);
        loadImg(energy ? "estorage" : "mstorage");
        this.energy = energy;
        preloadString(s -> {
            s[0] = DESCRIPTIONS[this.energy ? 3 : 2];
            setAmtValue(0, this.amount);
        });
        updateDescription();
    }
    
    public static StoragePower MakeStorage(boolean energy) {
        return new StoragePower(LMSK.Player(), 1, energy);
    }
    
    public static AbstractPower GetInstance(boolean energy) {
        return LMSK.Player().getPower(ID + (energy ? "ENERGY" : "MANA"));
    }
    
    @Override
    public void atStartOfTurnPostDraw() {
        addToBot(energy ? new GainEnergyAction(amount) : new ModifyManaAction(amount));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    public void instant() {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        addToTop(energy ? new GainEnergyAction(amount) : new ModifyManaAction(amount));
    }
    
    @Override
    public AbstractPower makeCopy() {
        return null;
    }
}