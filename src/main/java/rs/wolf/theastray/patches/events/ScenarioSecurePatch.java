package rs.wolf.theastray.patches.events;

import basemod.eventUtil.EventUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.events.dialogs.beyond.TheAstrayThirdDialogEvent;
import rs.wolf.theastray.events.dialogs.city.TheAstraySecondDialogEvent;
import rs.wolf.theastray.events.dialogs.exordium.TheAstrayFirstDialogEvent;
import rs.wolf.theastray.utils.TAUtils;

public class ScenarioSecurePatch {
    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class FirstEncounterPatch {
        @SpireRawPatch
        public static void Raw(CtBehavior ctBehavior) throws Exception {
            ClassPool pool = ctBehavior.getDeclaringClass().getClassPool();
            CtClass ctClass = ctBehavior.getDeclaringClass();
            CtMethod nRT = ctClass.getDeclaredMethod("nextRoomTransition", new CtClass[]{pool.get(SaveFile.class.getName())});
            nRT.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("roll".equals(m.getMethodName()) && EventHelper.class.getName().equals(m.getClassName())) {
                        m.replace("{if(" + FirstEncounterPatch.class.getName() +  ".ScenarioCheck($$))" +
                                "{$_=" + FirstEncounterPatch.class.getName() + ".PrepareScenarioEvent();}else{$_=$proceed($$);}}");
                    }
                }
            });
        }
        
        public static boolean ScenarioCheck(Random eventDup) {
            boolean second = AbstractDungeon.actNum == 2
                    && Leader.SaveData.getBool(TheAstraySecondDialogEvent.CONTINUES);
            
            boolean third = AbstractDungeon.actNum == 3
                    && Leader.SaveData.getBool(TheAstrayThirdDialogEvent.CONTINUES);
            
            return second || third;
        }
        
        //TODO: Add event to the event list
        public static EventHelper.RoomResult PrepareScenarioEvent() {
            return EventHelper.RoomResult.EVENT;
        }
    }
    
    @SpirePatch2(clz = EventRoom.class, method = "onPlayerEntry")
    public static class EventRoomPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("generateEvent".equals(m.getMethodName())) {
                        m.replace("{$_=" + EventRoomPatch.class.getName() + ".GetScenario($$);if($_==null)$_=$proceed($$);}");
                    }
                }
            };
        }
        
        public static AbstractEvent GetScenario(Random eventDup) {
            boolean second = AbstractDungeon.actNum == 2
                    && Leader.SaveData.getBool(TheAstraySecondDialogEvent.CONTINUES);
    
            boolean third = AbstractDungeon.actNum == 3
                    && Leader.SaveData.getBool(TheAstrayThirdDialogEvent.CONTINUES);
            
            if (second || third) {
                String eventName = null;
                for (String s : EventUtils.getDungeonEvents(AbstractDungeon.id).keySet()) {
                    if (s.contains(second ? "SecondScenarioEvent" : "ThirdScenarioEvent")) {
                        eventName = s;
                        TAUtils.Log("Find event [" + eventName + "]");
                        break;
                    }
                }
                if (eventName == null) {
                    TAUtils.Log("Couldn't find event in " + AbstractDungeon.id);
                    eventName = second ? TheAstraySecondDialogEvent.ID : TheAstrayThirdDialogEvent.ID;
                }
                boolean keyExisted = false;
                AbstractEvent event;
                try {
                    event = EventHelper.getEvent(eventName);
                    keyExisted = event != null;
                } catch (Exception e) {
                    event = second ? new TheAstraySecondDialogEvent() : new TheAstrayThirdDialogEvent();
                }
                if (!keyExisted)
                    TAUtils.Log("No key associated with Dialog Event");
                AbstractDungeon.specialOneTimeEventList.remove(eventName);
                AbstractDungeon.eventList.remove(eventName);
                Leader.SCENARIO_TMP_GOES = false;
                return event;
            }
            return null;
        }
    }
}