package rs.wolf.theastray.ui.manalayout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ManaMst implements TAUtils {
    private static final float MANA_CHARGE_COUNT = 1F;
    private static final UIStrings uiStrings = TAUtils.UIStrings("ManaMst");
    public static final String[] TEXT = uiStrings.TEXT;
    private final ArrayList<PowerTip> tips = new ArrayList<>();
    private final AbstractCreature owner;
    private final Hitbox ballHb;
    private final List<Mana> orbs = new ArrayList<>();
    private final List<Mana> removeList = new ArrayList<>();
    private final Map<Integer, NemesisSoulOrbit> orbitMap = new HashMap<>();
    private int maxMana;
    private int currMana;
    private int startingMana;
    private float manaChargePerTurn;
    private float manaCharge;
    private Layout layout;
    
    public ManaMst(AbstractCreature owner, int maxMana, int startingMana, float manaChargePerTurn, Layout layout) {
        this.owner = owner;
        this.maxMana = maxMana;
        this.currMana = this.startingMana = startingMana;
        this.manaChargePerTurn = manaChargePerTurn;
        this.manaCharge = 0F;
        this.layout = layout;
        this.ballHb = new Hitbox(scale(120F), scale(120F));
        addTip(TEXT[0], TEXT[1]);
    }
    
    public ManaMst(AbstractCreature owner, int maxMana, int startingMana, Layout layout) {
        this(owner, maxMana, startingMana, 0F, layout);
    }
    
    public ManaMst(AbstractCreature owner, int maxMana, int startingMana) {
        this(owner, maxMana, startingMana, Layout.LINE);
    }
    
    public enum Layout {
        LINE, REVOLUTION, ORBIT
    }
    
    public ManaMst addTip(String title, String body) {
        if (tips.stream().anyMatch(t -> t.header.equals(title))) {
            tips.stream().filter(t -> t.header.equals(title)).forEach(t -> t.body = body);
        } else {
            tips.add(new PowerTip(title, body));
        }
        return this;
    }
    
    public void gainMana(int amt) {
        AbstractPlayer p = LMSK.Player();
        if (p.hand.group.stream().anyMatch(c -> c.cardID.equals(GlobalIDMst.CardID(87))))
            return;
        if (currMana + amt > maxMana)
            amt = maxMana - currMana;
        amt = triggerCardsOnGainingMana(amt);
        if (amt <= 0) return;
        currMana += amt;
        selfGainMana(amt);
    }
    
    private int triggerCardsOnGainingMana(int amount) {
        AbstractPlayer p = LMSK.Player();
        for (AbstractCard card : p.drawPile.group) {
            if (card instanceof AstrayCard)
                amount = ((AstrayCard) card).modifyOnGainingMana(amount);
        }
        for (AbstractCard card : p.hand.group) {
            if (card instanceof AstrayCard)
                amount = ((AstrayCard) card).modifyOnGainingMana(amount);
        }
        for (AbstractCard card : p.discardPile.group) {
            if (card instanceof AstrayCard)
                amount = ((AstrayCard) card).modifyOnGainingMana(amount);
        }
        return amount;
    }
    
    private void selfGainMana(int amt) {
        if (orbs.isEmpty()) {
            orbs.add(new Mana(getManaPosition(0, 0)));
        } else {
            Mana last = orbs.get(orbs.size() - 1);
            for (int i = 0; i < amt; i++) {
                Mana mana = new Mana(last.cX, last.cY);
                orbs.add(mana);
            }
        }
        triggerCardsOnManaGained(amt);
        reorganizeManaOrbs();
    }
    
    private void triggerCardsOnManaGained(int amount) {
        AbstractPlayer p = LMSK.Player();
        for (AbstractCard card : p.drawPile.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onManaGained(amount);
        }
        for (AbstractCard card : p.hand.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onManaGained(amount);
        }
        for (AbstractCard card : p.discardPile.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onManaGained(amount);
        }
    }
    
    public void loseMana(int amt) {
        if (amt < 0) return;
        if (currMana < amt) amt = currMana;
        currMana -= amt;
        selfLoseMana(amt);
    }
    
    private void selfLoseMana(int amt) {
        for (int i = 0; i < amt; i++) {
            removeList.add(orbs.get(i));
            if (i >= orbs.size()) break;
        }
        triggerGearsOnManaLost(amt);
        reorganizeManaOrbs();
    }
    
    private void triggerGearsOnManaLost(int amount) {
        AbstractPlayer player = LMSK.Player();
        for (AbstractPower p : player.powers) {
            if (p instanceof AstrayPower)
                ((AstrayPower) p).onManaLost(amount);
        }
    }
    
    public int getCurrMana() {
        return currMana;
    }
    
    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }
    
    public int getMaxMana() {
        return maxMana;
    }
    
    public void setStartingMana(int startingMana) {
        this.startingMana = startingMana;
    }
    
    public int getStartingMana() {
        return startingMana;
    }
    
    public void setManaChargePerTurn(float manaChargePerTurn) {
        this.manaChargePerTurn = manaChargePerTurn;
    }
    
    public float getManaChargePerTurn() {
        return manaChargePerTurn;
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
            amt = currMana;
        }
        for (int i = 0; i < amt; i++) {
            Mana mana = orbs.get(i);
            playSound("ORB_PLASMA_EVOKE");
            AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(mana.cX, mana.cY));
        }
        loseMana(amt);
        if (currMana <= 0) {
            triggerCardsOnManaRunOut();
        }
    }
    
    private void triggerCardsOnManaRunOut() {
        AbstractPlayer p = LMSK.Player();
        for (AbstractCard card : p.drawPile.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onManaRunOut();
        }
        for (AbstractCard card : p.hand.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onManaRunOut();
        }
        for (AbstractCard card : p.discardPile.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onManaRunOut();
        }
    }
    
    private void reorganizeManaOrbs() {
        orbs.removeAll(removeList);
        removeList.clear();
        if (orbs.isEmpty()) return;
        if (orbs.size() != currMana) {
            TAUtils.Log("MANA ORB SIZE DOES NOT FIT CURRENT MANA !!!!");
            int diff = currMana - orbs.size();
            if (diff > 0) {
                Mana last = orbs.get(orbs.size() - 1);
                for (int i = 0; i < diff; i++) {
                    Mana mana = new Mana(last.cX, last.cY);
                    orbs.add(mana);
                }
            } else {
                currMana -= diff;
            }
        }
        float totalLen = scale(96F * 0.6F) * (currMana - 1);
        for (int i = 0; i < currMana; i++) {
            orbs.get(i).transAnim(getManaPosition(i, totalLen));
        }
    }
    
    public void atTurnStart() {
        if (currMana >= maxMana) return;
        manaCharge += manaChargePerTurn;
        if (manaCharge >= MANA_CHARGE_COUNT) {
            int charges = MathUtils.floor(manaCharge / MANA_CHARGE_COUNT);
            manaCharge -= charges;
            gainMana(charges);
        }
    }
    
    public void initPreBattle() {
        ballHb.move(owner.hb.x - scale(100F), owner.hb.cY);
        orbs.clear();
        currMana = 0;
        float totalLen = scale(96F * 0.6F) * (startingMana - 1);
        if (startingMana > 0) gainMana(startingMana);
        for (int i = 0; i < maxMana; i++) {
            NemesisSoulOrbit orbit = new NemesisSoulOrbit(i, this);
            orbitMap.put(i, orbit);
        }
    }
    
    public void clearPostBattle() {
        ballHb.move(-10F, -10F);
        orbs.clear();
        currMana = 0;
    }
    
    public void changeLayout(Layout layout) {
        if (this.layout == layout) return;
        this.layout = layout;
        float totalLen = scale(96F * 0.6F) * (orbs.size() - 1);
        for (int i = 0; i < orbs.size(); i++) {
            Vector2 pos = getManaPosition(i, totalLen);
            orbs.get(i).transAnim(pos);
        }
    }
    
    public Layout currLayout() {
        return layout;
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
            case ORBIT:
                NemesisSoulOrbit orbit;
                if (!orbitMap.containsKey(slot)) {
                    orbit = new NemesisSoulOrbit(slot, this);
                    orbitMap.put(slot, orbit);
                } else {
                    orbit = orbitMap.get(slot);
                }
                return orbit.getPosition();
            default:
                cY = owner.hb.y + owner.hb.height + owner.animY + scale(50F);
                cX = owner.hb.cX + totalLen / 2F - scale(57.6F) * slot;
                return new Vector2(cX, cY);
        }
    }
    
    public void update() {
        if (layout == Layout.ORBIT) {
            orbitMap.forEach((k,v) -> {
                Mana mana = null;
                if (k < orbs.size()) mana = orbs.get(k);
                v.update(mana);
            });
        }
        orbs.forEach(m -> {
            m.update();
            if (m.hb.hovered && !tips.isEmpty()) {
                TipHelper.queuePowerTips(m.tX + scale(96F), m.tY + scale(32F), tips);
            }
        });
    }
    
    public void render(SpriteBatch sb) {
        orbs.stream().filter(m -> removeList.isEmpty() || !removeList.contains(m)).forEach(m -> m.render(sb));
    }
    
    private static class NemesisSoulOrbit {
        private static final int minTicks = 200;
        private static final int maxTicks = 600;
        private static final float defVelocity = 0.05F;
        private final float perihelion;
        private final float aphelion;
        private final float leastVelocity;
        private int distanceTicks;
        private int currDistTicks;
        private int velocityTicks;
        private int currVeloTicks;
        private float phase;
        private float lastDist = 0;
        private float currDist;
        private float targetDist = 0;
        private float lastVelo = 0;
        private float currVelo = 0;
        private float targetVelo = 0;
        
        protected ManaMst manaMst;
        
        protected NemesisSoulOrbit(int slot, @NotNull ManaMst manaMst) {
            this.manaMst = manaMst;
            float orbit = manaMst.owner.hb.width / 2F + 96F * Settings.scale;
            orbit = orbit + slot * 96F * 0.75F * Settings.scale;
            perihelion = orbit * 1 / 3;
            aphelion = orbit * 2 / 3;
            phase = MathUtils.random((float) (Math.PI * 2));
            leastVelocity = (defVelocity - (defVelocity / (slot + 1)) * 0.02F) * (MathUtils.randomBoolean() ? 1 : -1);
            currDist = MathUtils.random(perihelion, aphelion);
        }
        
        protected void update(Mana soul) {
            if (currDistTicks < distanceTicks) {
                currDist = lastDist + sigmoid(targetDist - lastDist, 4F / distanceTicks, currDistTicks);
                currDistTicks++;
            } else {
                distanceTicks = MathUtils.random(minTicks, maxTicks);
                currDistTicks = -distanceTicks;
                lastDist = currDist;
                targetDist = MathUtils.random(perihelion, aphelion);
            }
            if (currVeloTicks < velocityTicks) {
                currVelo = lastVelo + sigmoid(targetVelo - lastVelo, 4F / velocityTicks, currVeloTicks);
                currVeloTicks++;
            } else {
                velocityTicks = MathUtils.random(minTicks, maxTicks);
                currVeloTicks = -velocityTicks;
                lastVelo = currVelo;
                targetVelo = leastVelocity;
            }
            phase += currVelo;
            if (soul != null) {
                soul.tX = (float) (currDist * Math.cos(phase) + manaMst.owner.drawX);
                soul.tY = (float) (currDist * Math.sin(phase) + manaMst.owner.drawY + manaMst.owner.hb.height / 2F);
            }
        }
        
        protected Vector2 getPosition() {
            return new Vector2((float) (currDist * Math.cos(phase) + manaMst.owner.drawX), (float) (currDist * Math.sin(phase) + manaMst.owner.drawY + manaMst.owner.hb.height / 2F));
        }
        
        private float sigmoid(float limit, float steepness, float curve) {
            return (float) (limit / (1 + Math.pow(Math.E, -(steepness * curve))));
        }
    }
}