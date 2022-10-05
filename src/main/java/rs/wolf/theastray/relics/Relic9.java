package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.abstracts.AstrayRelic;

public class Relic9 extends AstrayRelic {
    public Relic9() {
        super(9);
        counter = 3;
    }
    
    @Override
    public void atTurnStart() {
        if (counter > 0) {
            counter--;
            flash();
            atbTmpAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs()) {
                    addToTop(ApplyPower(m, cpr(), frostPower(m, cpr(), 2)));
                }
            });
            if (counter == 0)
                grayscale = true;
        }
    }
    
    @Override
    public void onVictory() {
        counter = 3;
        grayscale = false;
    }
}