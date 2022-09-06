package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.utils.GlobalIDMst;

public class Relic2 extends AstrayRelic {
    public Relic2() {
        super(2);
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (!usedUp) {
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            addToBot(new ModifyManaAction(10));
            addToBot(new GainEnergyAction(1));
            addToBot(new DrawCardAction(cpr(), 1));
            usedUp();
        }
    }
    
    @Override
    public void onVictory() {
        super.onVictory();
        usedUp = false;
        grayscale = false;
    }
    
    @Override
    public void obtain() {
        cpr().relics.stream().filter(r -> r instanceof Relic1)
                .findFirst()
                .map(r -> cpr().relics.indexOf(r))
                .ifPresent(i -> instantObtain(cpr(), i, true));
    }
    
    @Override
    public boolean canSpawn() {
        return cpr().hasRelic(GlobalIDMst.RelicID(1));
    }
}