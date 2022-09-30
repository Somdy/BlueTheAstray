package rs.wolf.theastray.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.utils.TAUtils;

public class Relic7 extends AstrayRelic {
    private static final float HP_THRESHOLD = 0.75F;
    private static boolean onPulse;
    
    public Relic7() {
        super(7);
    }
    
    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], SciPercent(HP_THRESHOLD));
    }
    
    @Override
    public void onEnterRestRoom() {
        onPulse = cpr().currentHealth > cpr().maxHealth * HP_THRESHOLD;
    }
    
    @Override
    public void update() {
        super.update();
        if (TAUtils.RoomAvailable()) {
            if (cpr().currentHealth > cpr().maxHealth * HP_THRESHOLD) {
                beginLongPulse();
            } else {
                stopPulse();
            }
        }
    }
    
    @SpirePatch(clz = CampfireSmithEffect.class, method = "update")
    public static class TravellerKitPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor(){
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("open".equals(m.getMethodName()) && GridCardSelectScreen.class.getName().equals(m.getClassName())) {
                        m.replace("{if(" + TravellerKitPatch.class.getName() + ".UsingTravellerKit()){" +
                                "$_=$proceed($1,2,$3,$4,$5,$6,$7);}else{$_=$proceed($$);}}");
                    }
                }
            };
        }
        public static boolean UsingTravellerKit() {
            return onPulse;
        }
    }
}
