package rs.wolf.theastray.relics;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.utils.TAUtils;

public class Relic5 extends AstrayRelic {
    private boolean onPulse;
    
    public Relic5() {
        super(5);
        onPulse = true;
    }
    
    @Override
    public void atPreBattle() {
        onPulse = true;
        beginLongPulse();
    }
    
    @Override
    public void atTurnStart() {
        if (!onPulse) {
            onPulse = true;
            beginLongPulse();
        }
    }
    
    @Override
    public void onTrigger() {
        onPulse = false;
        stopPulse();
    }
    
    @Override
    public void update() {
        super.update();
        if (TAUtils.RoomAvailable() && onPulse && cpr().hand.size() >= BaseMod.MAX_HAND_SIZE) {
            onTrigger();
            addToBot(new ModifyManaAction(1));
            addToBot(new GainEnergyAction(1));
        }
    }
}