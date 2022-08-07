package rs.wolf.theastray.ui.manalayout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import org.jetbrains.annotations.NotNull;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ManaMst implements TAUtils {
    private final AbstractCreature owner;
    private final Hitbox ballHb;
    private final List<Mana> orbs = new ArrayList<>();
    private final List<Mana> removeList = new ArrayList<>();
    private final int maxMana;
    private int currMana;
    private final int startingMana;
    private Layout layout;
    
    public ManaMst(AbstractCreature owner, int maxMana, int startingMana, Layout layout) {
        this.owner = owner;
        this.maxMana = maxMana;
        this.currMana = this.startingMana = startingMana;
        this.layout = layout;
        this.ballHb = new Hitbox(scale(120F), scale(120F));
    }
    
    public ManaMst(AbstractCreature owner, int maxMana, int startingMana) {
        this(owner, maxMana, startingMana, Layout.LINE);
    }
    
    public enum Layout {
        LINE, REVOLUTION
    }
    
    public void gainMana(int amt) {
        if (currMana + amt > maxMana)
            amt = maxMana - currMana;
        if (amt <= 0) return;
        currMana += amt;
        selfGainMana(amt);
    }
    
    private void selfGainMana(int amt) {
        Mana last = orbs.get(orbs.size() - 1);
        for (int i = 0; i < amt; i++) {
            Mana mana = new Mana(last.cX, last.cY);
            orbs.add(mana);
        }
        reorganizeManaOrbs();
    }
    
    public void loseMana(int amt) {
        if (currMana < amt || amt < 0) return;
        currMana -= amt;
        selfLoseMana(amt);
    }
    
    private void selfLoseMana(int amt) {
        for (int i = 0; i < amt; i++) {
            removeList.add(orbs.get(i));
            if (i >= orbs.size()) break;
        }
        reorganizeManaOrbs();
    }
    
    public boolean canUseMana(int amt) {
        return currMana >= amt && orbs.size() >= amt;
    }
    
    public boolean hasMana() {
        return currMana > 0;
    }
    
    public void useMana(int amt) {
        if (amt > currMana) {
            TAUtils.Log(this, "current mana is less than [" + amt + "]");
            return;
        }
        for (int i = 0; i < amt; i++) {
            Mana mana = orbs.get(i);
            playSound("ORB_PLASMA_EVOKE");
            AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(mana.cX, mana.cY));
        }
        loseMana(amt);
    }
    
    private void reorganizeManaOrbs() {
        orbs.removeAll(removeList);
        removeList.clear();
        float totalLen = scale(96F * 0.6F) * (currMana - 1);
        for (int i = 0; i < currMana; i++) {
            orbs.get(i).transAnim(getManaPosition(i, totalLen));
        }
    }
    
    public void initPreBattle() {
        ballHb.move(owner.hb.x - scale(100F), owner.hb.cY);
        orbs.clear();
        float totalLen = scale(96F * 0.6F) * (startingMana - 1);
        for (int i = 0; i < startingMana; i++) {
            Mana mana = new Mana(getManaPosition(i, totalLen));
            orbs.add(mana);
        }
    }
    
    @NotNull
    private Vector2 getManaPosition(int slot, float totalLen) {
        float cX, cY;
        switch (layout) {
            case REVOLUTION:
                float dist = scale(140F) + maxMana * scale(10F);
                float angle = 360F;
                angle *= slot / (currMana * 1F);
                cX = dist * MathUtils.cosDeg(angle) + owner.drawX;
                cY = dist * MathUtils.sinDeg(angle) + owner.drawY + owner.hb.height / 2F;
                return new Vector2(cX, cY);
            default:
                cY = owner.hb.y + owner.hb.height + owner.animY + scale(50F);
                cX = owner.hb.cX + totalLen / 2F - scale(57.6F) * slot;
                return new Vector2(cX, cY);
        }
    }
    
    public void update() {
        orbs.forEach(Mana::update);
    }
    
    public void render(SpriteBatch sb) {
        orbs.stream().filter(m -> removeList.isEmpty() || !removeList.contains(m)).forEach(m -> m.render(sb));
    }
}