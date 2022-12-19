package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import rs.wolf.theastray.characters.BlueTheAstray;

import java.util.ArrayList;

public class SensoryStonePatch {
    @SpirePatch2(clz = SensoryStone.class, method = "getRandomMemory")
    public static class AddExtraMemoryPatch {
        @SpireInsertPatch(rloc = 1, localvars = {"memories"})
        public static void Insert(ArrayList<String> memories) {
            memories.add(BlueTheAstray.SENSORY_MEMORY);
        }
    }
}
