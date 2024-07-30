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
        if (!TAUtils.RoomChecker(room, MonsterRoom.class)) {
            int heal = 6;
            if (TAUtils.RoomChecker(room, RestRoom.class)) 
                heal += 6;
            flash();
            cpr().heal(heal);
        }
    }
}