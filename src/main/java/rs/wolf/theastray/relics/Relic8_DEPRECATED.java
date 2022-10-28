package rs.wolf.theastray.relics;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.Optional;

@Deprecated
@AutoAdd.Ignore
public class Relic8_DEPRECATED extends AstrayRelic {
    public Relic8_DEPRECATED() {
        super(8);
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (TAUtils.RoomChecker(MonsterRoomBoss.class)) {
            usedUp();
            counter = -10;
        }
    }
    
    protected boolean shouldCheckReward() {
        return !usedUp && counter == -10;
    }
    
    public static void RewardCheck(ArrayList<AbstractCard> cardList) {
        int least = 2;
        if (!cardList.isEmpty()) {
            least = Math.min(least, cardList.size());
            int count = 0;
            for (AbstractCard card : cardList) {
                if (card.upgraded) count++;
                if (count >= least) break;
            }
            int diff = least - count;
            if (diff > 0) {
                LMSK.Player().getRelic(GlobalIDMst.RelicID(8)).flash();
                for (int i = 0; i < diff; i++) {
                    Optional<AbstractCard> opt = LMSK.GetRandom(cardList, LMSK.CardRandomRng());
                    opt.ifPresent(AbstractCard::upgrade);
                }
            }
        }
    }
    
//    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class BadgePatch {
        @SpirePostfixPatch
        public static void Postfix(ArrayList<AbstractCard> __result) {
            if (LMSK.Player().hasRelic(GlobalIDMst.RelicID(8))) {
                Relic8_DEPRECATED r = (Relic8_DEPRECATED) LMSK.Player().getRelic(GlobalIDMst.RelicID(8));
                if (r.shouldCheckReward()) 
                    Relic8_DEPRECATED.RewardCheck(__result);
            }
        }
    }
}