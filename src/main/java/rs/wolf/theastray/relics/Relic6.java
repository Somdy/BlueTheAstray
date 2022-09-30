package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.utils.TAUtils;

public class Relic6 extends AstrayRelic {
    private static final int HEAL_AMT = 24;
    
    public Relic6() {
        super(6);
    }
    
    @Override
    public void onVictory() {
        if (TAUtils.RoomChecker(MonsterRoomElite.class)) {
            flash();
            cpr().heal(HEAL_AMT, true);
        }
    }
}
