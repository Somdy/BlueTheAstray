package rs.wolf.theastray.cards.curses;

import rs.wolf.theastray.cards.AstrayCurseCard;
import rs.wolf.theastray.powers.unique.DroughtPower;

public class C86 extends AstrayCurseCard {
    public C86() {
        super(86);
        isEthereal = true;
    }
    
    @Override
    public void triggerWhenDrawn() {
        addToTop(ApplyPower(cpr(), cpr(), new DroughtPower()));
    }
}
