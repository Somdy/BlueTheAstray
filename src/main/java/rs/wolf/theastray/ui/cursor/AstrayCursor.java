package rs.wolf.theastray.ui.cursor;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import rs.wolf.theastray.utils.TAImageMst;
import rs.wolf.theastray.vfx.misc.CursorTrailEffect;

import java.util.ArrayList;
import java.util.List;

public class AstrayCursor extends GameCursor {
    private static final float OFFSET_X = 24.0F * Settings.scale;
    private static final float OFFSET_Y = -24.0F * Settings.scale;
    
    private List<CursorTrailEffect> effects = new ArrayList<>();
    private float effectTimer = 0F;
    private float timer = 0F;
    private float speedTimer = 0F;
    private Vector2 lastPos;
    public Vector2 speed = new Vector2();
    public final Color glowColor = new Color(0.0F, 0.8F, 1.0F, 1.0F);
    
    public AstrayCursor() {
        ReflectionHacks.setPrivateInherited(this, AstrayCursor.class, "img", TAImageMst.CURSOR);
    }
    
    @Override
    public void update() {
        timer += Gdx.graphics.getDeltaTime();
        super.update();
        glowColor.g = 0.8F + MathUtils.sinDeg(timer * 200.0F) / 5.0F;
        updateSpeed();
        updateEffects();
    }
    
    private void updateSpeed() {
        if (speedTimer <= 0.0F) {
            speedTimer = 0.01F;
            if (lastPos != null) {
                speed.set(InputHelper.mX - lastPos.x, InputHelper.mY - lastPos.y);
                speed.x /= 0.5F;
                speed.y /= 0.5F;
            }
            lastPos = new Vector2(InputHelper.mX, InputHelper.mY);
        } else {
            speedTimer -= Gdx.graphics.getDeltaTime();
        }
    }
    
    private void updateEffects() {
        if (effectTimer <= 0.0F) {
            if (true || speed.len() <= 25.0F && effects.size() <= 6 || effects.size() <= 12) {
                effects.add(new CursorTrailEffect(InputHelper.mX - 32.0F + OFFSET_X, InputHelper.mY - 32.0F + OFFSET_Y, speed));
            }
            effectTimer = 0.15F;
            if (speed.len() > 25.0F)
                effectTimer /= speed.len() * 0.2F;
        }
        if (effectTimer > 0.0F) {
            effectTimer -= Gdx.graphics.getDeltaTime();
        }
//        if (speed == null || speed.len() <= 10F) {
//            effects.forEach(CursorTrailEffect::speedUpFading);
//        }
        effects.forEach(AbstractGameEffect::update);
        effects.removeIf(e -> e.isDone);
    }
    
    @Override
    public void render(SpriteBatch sb) {
        if (hidden || Settings.isControllerMode)
            return;
        CursorType type = ReflectionHacks.getPrivate(this, GameCursor.class, "type");
        float rotation = ReflectionHacks.getPrivate(this, GameCursor.class, "rotation");
        if (!Settings.isTouchScreen || Settings.isDev) {
            effects.forEach(e -> e.render(sb));
            if (type == CursorType.NORMAL) {
                // render outer glow
                sb.setColor(glowColor.cpy());
                sb.draw(TAImageMst.CURSOR_GLOW, InputHelper.mX - 32.0F + OFFSET_X, InputHelper.mY - 32.0F + OFFSET_Y, 
                        32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 
                        rotation, 0, 0, 64, 64, false, false);
            }
        }
        super.render(sb);
        if (!Settings.isTouchScreen || Settings.isDev) {
            if (type == CursorType.NORMAL) {
                // render badge
                sb.setColor(Color.WHITE.cpy());
                sb.draw(TAImageMst.CURSOR_BADGE, InputHelper.mX - 32.0F + OFFSET_X, InputHelper.mY - 32.0F + OFFSET_Y,
                        32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale,
                        rotation, 0, 0, 64, 64, false, false);
            }
        }
    }
}