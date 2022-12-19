package rs.wolf.theastray.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rs.lazymankits.LManager;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.wolf.theastray.utils.TAImageMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class FallingStarEffect extends LMCustomGameEffect implements TAUtils {
    private final float tX;
    private final float tY;
    private float sX;
    private float sY;
    private final Vector2 speed;
    private float rotation;
    private Texture image = TAImageMst.TINY_STAR;
    private Texture glow = TAImageMst.TINY_STAR_GLOW_MASK;
    private Color glowColor;
    private List<FallingStarTrailEffect> trails = new ArrayList<>();
    private boolean firstFrame;
    
    public FallingStarEffect(AbstractCreature target) {
        this.tX = MathUtils.random(target.hb.x + target.hb.width / 3, target.hb.x + target.hb.width * 2 / 3);
        this.tY = target.hb.cY;
        sX = this.tX;
        sY = this.tY + Settings.HEIGHT / 2F;
        rotation = MathUtils.random(360F);
        duration = 0.5F;
        LManager.gameFps.calculate();
        float frames = (float) LManager.gameFps.getCurrFps();
        speed = new Vector2((Math.abs(sX - this.tX)) / frames, Math.abs(sY - this.tY) / frames);
        speed.x *= 1.25F;
        speed.y *= 1.25F;
        color = Color.WHITE.cpy();
        glowColor = Color.SKY.cpy();
        glowColor.a = 0.5F;
        firstFrame = true;
    }
    
    @Override
    public void update() {
        if (firstFrame) {
            playSound(TAUtils.MakeID("FALLING_STAR"));
            firstFrame = false;
        }
        duration -= Gdx.graphics.getDeltaTime();
        sX = lerp(sX, tX, Gdx.graphics.getDeltaTime() * speed.x);
        sY = lerp(sY, tY, Gdx.graphics.getDeltaTime() * speed.y);
        rotation += Gdx.graphics.getDeltaTime() * 20F;
        trails.add(new FallingStarTrailEffect(sX, sY, rotation));
        trails.forEach(FallingStarTrailEffect::update);
        if (duration < 0F || (sY == tY)) {
            isDone = true;
            color.a = 0F;
            trails.forEach(FallingStarTrailEffect::disappear);
        }
        trails.removeIf(e -> e.isDone);
    }
    
    private float lerp(float from, float to, float progress) {
        from = from + (to - from) * progress;
        if (Math.abs(to - from) <= 0.1F) from = to;
        return from;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(image, sX - 18F, sY - 18F, 18F, 18F, 36, 36, scale, scale, 
                rotation, 0, 0, 36, 36, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_TRUE);
        sb.setColor(glowColor);
        sb.draw(glow, sX - 18F, sY - 18F, 18F, 18F, 36, 36, scale * 1.2F, scale * 1.2F,
                rotation, 0, 0, 36, 36, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        trails.forEach(e -> e.render(sb));
    }
    
    @Override
    public void dispose() {
        
    }
}