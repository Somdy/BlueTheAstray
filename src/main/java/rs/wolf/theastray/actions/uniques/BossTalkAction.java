package rs.wolf.theastray.actions.uniques;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.localizations.TADialogDictLocals;
import rs.wolf.theastray.monsters.BlueTheBoss;

import java.util.Arrays;

public class BossTalkAction extends AstrayGameAction {
    private final String currWords;
    private TADialogDictLocals.Dialog[] nextDialogs;
    private boolean used;
    private final float bubbleDuration;
    private BlueTheBoss boss;
    private BlueTheBoss.FinalWill bossWill;
    
    public BossTalkAction(BlueTheBoss theBoss, TADialogDictLocals.Dialog[] dialogs, BlueTheBoss.FinalWill will) {
        setValues(theBoss, theBoss);
        boss = theBoss;
        bossWill = will;
        currWords = dialogs[0].words;
        if (dialogs.length > 1) {
            nextDialogs = Arrays.copyOfRange(dialogs, 1, dialogs.length);
        }
        used = false;
        this.duration = startDuration = MathUtils.random(dialogs[0].minDur, dialogs[0].maxDur);
        this.bubbleDuration = startDuration;
    }
    
    public BossTalkAction(BlueTheBoss source, TADialogDictLocals.Dialog dialog, BlueTheBoss.FinalWill will) {
        this(source, new TADialogDictLocals.Dialog[]{dialog}, will);
    }
    
    @Override
    public void update() {
        if (!used) {
            used = true;
            effectToList(new SpeechBubble(source.hb.cX + source.dialogX, source.hb.cY + source.dialogY, bubbleDuration, currWords, false));
        }
        tickDuration();
        if (isDone) {
            if (nextDialogs != null && nextDialogs.length > 0) {
                addToTop(new BossTalkAction(boss, nextDialogs, bossWill));
            } else {
                bossWill.finish();
            }
        }
    }
}