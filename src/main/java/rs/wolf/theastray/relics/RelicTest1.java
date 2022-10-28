package rs.wolf.theastray.relics;

import rs.wolf.theastray.abstracts.AstrayRelic;

public class RelicTest1 extends AstrayRelic {
    public RelicTest1() {
        super("RelicTest1");
        counter = -1;
    }
    
    @Override
    public void atBattleStart() {
        if (counter == -1) {
            beginLongPulse();
        }
    }
    
    @Override
    protected boolean onRightClick() {
        if (counter == -1) {
            counter = 0;
            stopPulse();
        } else {
            counter = -1;
            beginLongPulse();
        }
        return super.onRightClick();
    }
}