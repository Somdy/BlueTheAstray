package rs.wolf.theastray.ui.manalayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;
import org.jetbrains.annotations.NotNull;
import rs.wolf.theastray.utils.TAImageMst;
import rs.wolf.theastray.utils.TAUtils;

public class Mana extends AbstractOrb implements TAUtils {
    private static final float STOP_ANIM = -99999F;
    private static final float ORB_WAVY_DIST = 0.04F;
    private static final float vfxIntervalMin = 0.1F;
    private static final float vfxIntervalMax = 0.4F;
    private float vfxTimer = 1.0F;
    private float animX;
    private float animY;
    private float animSpeed;
    
    public Mana(@NotNull Vector2 pos) {
        cX = tX = animX = pos.x;
        cY = tY = animY = pos.y;
        hb.move(pos.x, pos.y);
        angle = MathUtils.random(360F);
        img = TAImageMst.MANA;
    }
    
    public Mana(float cX, float cY) {
        this(new Vector2(cX, cY));
    }
    
    @Override
    public void updateDescription() {}
    
    @Override
    public void onEvoke() {}
    
    public void transAnim(float diffX, float diffY, float speed) {
        animX = tX + diffX;
        animY = tY + diffY;
        animSpeed = speed;
    }
    
    public void transAnim(float tX, float tY) {
        tX -= this.tX;
        tY -= this.tY;
        transAnim(tX, tY, 6F);
    }
    
    public void transAnim(@NotNull Vector2 target) {
        transAnim(target.x, target.y);
    }
    
    protected Mana move(@NotNull Vector2 target) {
        this.tX = target.x;
        this.tY = target.y;
        hb.move(tX, tY);
        return this;
    }
    
    @Override
    public void update() {
        hb.move(tX, tY);
        hb.update();
        bobEffect.update();
        cX = MathHelper.orbLerpSnap(cX, tX);
        cY = MathHelper.orbLerpSnap(cY, tY);
        if (channelAnimTimer != 0.0F) {
            channelAnimTimer -= Gdx.graphics.getDeltaTime();
            if (channelAnimTimer < 0.0F) {
                channelAnimTimer = 0.0F;
            }
        }
        c.a = Interpolation.pow2In.apply(1.0F, 0.01F, channelAnimTimer / 0.5F);
        scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, channelAnimTimer / 0.5F);
        angle += Gdx.graphics.getDeltaTime() * 45.0F;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0F) {
            effectToList(new PlasmaOrbPassiveEffect(cX, cY));
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
        updateSelfAnimation();
    }
    
    private void updateSelfAnimation() {
        if (animX != STOP_ANIM) {
            tX = MathUtils.lerp(tX, animX, Gdx.graphics.getDeltaTime() * animSpeed);
            if (Math.abs(tX - animX) <= Settings.UI_SNAP_THRESHOLD) {
                tX = animX;
                animX = STOP_ANIM;
            }
        }
        if (animY != STOP_ANIM) {
            tY = MathUtils.lerp(tY, animY, Gdx.graphics.getDeltaTime() * animSpeed);
            if (Math.abs(tY - animY) <= Settings.UI_SNAP_THRESHOLD) {
                tY = animY;
                animY = STOP_ANIM;
            }
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        c.a /= 2F;
        sb.setColor(c);
        sb.draw(img, cX - 48F, cY - 48F + bobEffect.y, 48F, 48F, 96F, 96F,
                scale + MathUtils.sinDeg(angle) * ORB_WAVY_DIST * Settings.scale, scale, angle,
                0, 0, 96, 96, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_TRUE);
        sb.draw(img, cX - 48F, cY - 48F + bobEffect.y, 48F, 48F, 96F, 96F,
                scale, scale + MathUtils.sinDeg(angle) * ORB_WAVY_DIST * Settings.scale, -angle,
                0, 0, 96, 96, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        hb.render(sb);
    }
    
    @Override
    public void playChannelSFX() {
        playSound("ORB_PLASMA_CHANNEL");
    }
    
    @Override
    public AbstractOrb makeCopy() {
        return new Mana(new Vector2(cX, cY));
    }
}
