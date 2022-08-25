package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.actions.commons.ModifyManaAction;

public class Relic1 extends AstrayRelic {
    public Relic1() {
        super(1);
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        addToBot(new RelicAboveCreatureAction(cpr(), this));
        addToBot(new ModifyManaAction(1));
    }
}