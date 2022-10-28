package rs.wolf.theastray.vfx.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lazymankits.abstracts.LMCustomGameEffect;

public class ShiningStarEffect extends LMCustomGameEffect {
    private float x;
    private float y;
    private TextureAtlas.AtlasRegion img;
    
    public ShiningStarEffect(float x, float y) {
        duration = MathUtils.random(0.5F, 1F);
        startingDuration = duration;
        img = ImageMaster.ROOM_SHINE_2;
        this.x = x - img.packedWidth / 2F;
        this.y = y - this.img.packedHeight / 2F;
        scale = Settings.scale * MathUtils.random(0.5F, 1F);
        rotation = 0F;
        color = new Color(MathUtils.random(0.2F, 0.4F), MathUtils.random(0.8F, 1F), MathUtils.random(0.8F, 1F), 0.01F);
    }
    
    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0F) {
            isDone = true;
        }
        color.a = Interpolation.fade.apply(0.0F, 0.5F, duration / startingDuration);
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(img, x, y, img.packedWidth / 2F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, 
                scale * MathUtils.random(6.0F, 12.0F), scale * MathUtils.random(0.7F, 0.8F), 
                rotation + MathUtils.random(-1.0F, 1.0F));
        sb.draw(img, x, y, img.packedWidth / 2F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, 
                scale * MathUtils.random(0.2F, 0.5F), scale * MathUtils.random(2.0F, 3.0F), 
                rotation + MathUtils.random(-1.0F, 1.0F));
        sb.setBlendFunction(770, 771);
    }
    
    @Override
    public void dispose() {
        
    }
}
