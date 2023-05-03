package rs.wolf.theastray.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.GlobalManaMst;
import rs.wolf.theastray.utils.TAUtils;

public class ManaFieldPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("ManaFieldPower");
    
    public ManaFieldPower(int blocks) {
        super(ID, "manafield", PowerType.BUFF, AbstractDungeon.player);
        setValues(blocks);
        preloadString(s -> setAmtValue(0, amount));
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        if (amount > 0 && GlobalManaMst.CurrentMana() > 0) {
            flash();
            int blocks = amount * GlobalManaMst.CurrentMana();
            addToBot(new GainBlockAction(owner, blocks));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ManaFieldPower(amount);
    }
}
