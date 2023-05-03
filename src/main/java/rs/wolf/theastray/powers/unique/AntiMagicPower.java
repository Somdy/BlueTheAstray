package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.listeners.ApplyPowerListener;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.utils.TAUtils;

public class AntiMagicPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("AntiMagicPower");
    private int mplrID;
    
    public AntiMagicPower(AbstractCreature owner) {
        super(ID, "antimagic", PowerType.DEBUFF, owner);
        setValues(-1);
        updateDescription();
    }
    
    @Override
    public void onInitialApplication() {
        mplrID = ApplyPowerListener.AddNewManipulator(ID.length(), 0, e -> owner.hasPower(ID), (p, src, tgt) -> {
            if (p instanceof MagicPower && tgt == owner) {
                flash();
                p = null;
            }
            return p;
        });
    }
    
    @Override
    public void onRemove() {
        ApplyPowerListener.RemoveManipulator(mplrID);
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new AntiMagicPower(owner);
    }
}
