package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.wolf.theastray.abstracts.AstrayRelic;

public class Relic3 extends AstrayRelic {
    public Relic3() {
        super(3);
        counter = 0;
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void onPlayerEndTurn() {
        flash();
        counter = 0;
    }
    
    @Override
    public void onVictory() {
        counter = 0;
    }
    
    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        counter++;
        if (counter >= 8) {
            flash();
            counter = 0;
            addToBot(new GainEnergyAction(1));
        }
    }
}
