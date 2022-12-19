package rs.wolf.theastray.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.wolf.theastray.utils.TAImageMst;

public class FallingStarTrailEffect extends LMCustomGameEffect {
    private float x;
    private float y;
    private Texture image = TAImageMst.TINY_STAR;
    
    protected FallingStarTrailEffect(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        color = Color.SKY.cpy();
        color.a = 0.5F;
        startingDuration = duration = 0.5F;
    }
    
    protected void disappear() {
        color.a = 0F;
        isDone = true;
    }
    
    @Override
    public void update() {
        super.update();
        scale = scale * duration / startingDuration;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_TRUE);
        sb.setColor(color);
        sb.draw(image, x - 18F, y - 18F, 18F, 18F, 36, 36, scale * 1.2F, scale * 1.2F,
                rotation, 0, 0, 36, 36, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    @Override
    public void dispose() {
        
    }
}