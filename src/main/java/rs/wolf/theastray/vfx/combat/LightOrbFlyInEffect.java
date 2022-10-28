package rs.wolf.theastray.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lazymankits.abstracts.LMCustomGameEffect;

public class LightOrbFlyInEffect extends LMCustomGameEffect {
    private static final float DIST_THRESHOLD = 30F * Settings.scale;
    private float speed;
    private float speedStart;
    private float speedTarget;
    private float targetScale;
    private TextureAtlas.AtlasRegion img;
    private Vector2 pos;
    private Vector2 target;
    
    public LightOrbFlyInEffect(float startX, float startY, float destX, float destY, Color color) {
        img = ImageMaster.GLOW_SPARK_2;
        scale = MathUtils.random(1F, 1.4F) * Settings.scale;
        targetScale = scale * 2F;
        pos = new Vector2(startX - img.packedWidth / 2F, startY - img.packedHeight / 2F);
        target = new Vector2(destX, destY);
        startingDuration = duration = 5F;
        this.color = color;
        renderBehind = MathUtils.randomBoolean();
        speedTarget = new Vector2(startX - destX, startY - destY).len() / duration;
        speedStart = MathUtils.random(speedTarget / 1.5F, speedStart / 2F);
        speed = speedStart;
    }
    
    @Override
    public void update() {
        Vector2 tmp = new Vector2(pos.x - target.x, pos.y - target.y);
        tmp.x *= speed * Gdx.graphics.getDeltaTime();
        tmp.y *= speed * Gdx.graphics.getDeltaTime();
        speed = MathUtils.lerp(speed, speedTarget, Gdx.graphics.getDeltaTime() * (startingDuration - duration) / startingDuration);
        scale = MathUtils.lerp(scale, targetScale, 3F * Gdx.graphics.getDeltaTime() * (startingDuration - duration / startingDuration));
        pos.sub(tmp);
        scale += Gdx.graphics.getDeltaTime();
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0F || target.dst(pos) < DIST_THRESHOLD) {
            isDone = true;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(img, pos.x, pos.y, img.packedWidth / 2F, img.packedHeight / 2F, img.packedWidth, img.packedHeight, 
                scale, scale, 0);
        sb.setBlendFunction(770, 771);
    }
    
    @Override
    public void dispose() {}
}