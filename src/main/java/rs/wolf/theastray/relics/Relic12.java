package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.utils.TAUtils;

public class Relic12 extends AstrayRelic {
    public Relic12() {
        super(12);
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (!TAUtils.RoomChecker(MonsterRoom.class)) {
            int heal = 3;
            if (TAUtils.RoomChecker(RestRoom.class)) 
                heal += 3;
            flash();
            cpr().heal(heal);
        }
    }
}
