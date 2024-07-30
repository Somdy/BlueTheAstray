package rs.wolf.theastray.vfx.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import rs.wolf.theastray.utils.TAImageMst;

public class CursorTrailEffect extends AbstractGameEffect {
    private static int Index = 0;
    private Texture img;
    private Vector2 pos;
    private Vector2 inertia;
    private Vector2 falling;
    private boolean speedup = false;
    private float rotation;
    private float rotSpeed;
    private float timer = 0F;
    private float fadeSpeed = 6.0F;
    private Color maskColor;
    private float globalAlpha = 0.25F;
    
    public CursorTrailEffect(float x, float y, Vector2 speed) {
        pos = new Vector2(x, y);
        int index = Index++ % TAImageMst.CURSOR_TRAILS.length;
        if (MathUtils.randomBoolean() && index < TAImageMst.CURSOR_TRAILS.length - 1)
            index = MathUtils.random(index, TAImageMst.CURSOR_TRAILS.length - 1);
        img = TAImageMst.CURSOR_TRAILS[index];
        rotation = MathUtils.random(360F);
        rotSpeed = speed != null && speed.len() > 0 ? speed.len() * MathUtils.random(0.5F, 0.65F)
                : MathUtils.random(-5.0F, 5.0F);
        falling = new Vector2(0F, -10.0F);
        if (speed != null) {
            rotSpeed *= speed.x > 0 ? -1.0F : 1.0F;
            inertia = speed.cpy().nor();
            fadeSpeed = MathUtils.clamp(speed.len() * 0.5F, 2.0F, 5.0F);
        }
        color = Color.WHITE.cpy();
        maskColor = new Color(0.0F, MathUtils.random(0.5F, 0.7F), MathUtils.random(0.85F, 1.0F), 0.5F);
        scale = MathUtils.random(0.75F, 1F) * Settings.scale;
    }
    
    public void speedUpFading() {
        if (!speedup && inertia != null && inertia.len() > 0.0F) {
            speedup = true;
            fadeSpeed *= 2.0F;
        }
    }
    
    @Override
    public void update() {
        timer += Gdx.graphics.getDeltaTime();
        maskColor.a = 0.5F + MathUtils.sin(timer * Math.abs(rotSpeed) * 100.0F) / 10.0F;
        color.a = MathUtils.clamp(MathUtils.sin(timer * fadeSpeed) + 0.5F, 0.0F, 1.0F);
        maskColor.a *= globalAlpha;
        color.a *= globalAlpha;
        rotation += Gdx.graphics.getDeltaTime() * rotSpeed;
        pos.x += falling.x * Gdx.graphics.getDeltaTime();
        pos.y += falling.y * Gdx.graphics.getDeltaTime();
        if (inertia != null) {
            pos.x += inertia.x * Gdx.graphics.getDeltaTime() * Math.abs(rotSpeed) * 0.25F;
            pos.y += inertia.y * Gdx.graphics.getDeltaTime() * Math.abs(rotSpeed) * 0.25F;
        }
        falling.y -= Gdx.graphics.getDeltaTime() * timer * 50.0F * Settings.scale;
        if (color.a <= 0.0F) {
            isDone = true;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(img, pos.x, pos.y, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
                rotation, 0, 0, 64, 64, false, false);
        sb.setColor(maskColor);
        sb.draw(img, pos.x + MathUtils.random(-3.5F, 3.5F) * Settings.scale, pos.y + MathUtils.random(-3.5F, 3.5F) * Settings.scale, 
                32.0F, 32.0F, 64.0F, 64.0F, scale * 1.2F, scale * 1.2F,
                rotation, 0, 0, 64, 64, false, false);
    }
    
    @Override
    public void dispose() { }
}