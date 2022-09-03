package rs.wolf.theastray.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.lazymankits.abstracts.LMCustomGameEffect;

public class ManaShotEffect extends LMCustomGameEffect {
    private float sX;
    private float sY;
    private float tX;
    private float tY;
    private int count;
    private float timer = 0F;
    
    public ManaShotEffect(float sX, float sY, float tX, float tY, int count) {
        this.sX = sX - 20F * Settings.scale;
        this.sY = sY + 80F * Settings.scale;
        this.tX = tX;
        this.tY = tY;
        this.count = count;
    }
    
    @Override
    public void update() {
        timer -= Gdx.graphics.getDeltaTime();
        if (timer < 0F) {
            timer += MathUtils.random(0.05F, 0.15F);
            AbstractDungeon.effectsQueue.add(new ManaShotParticleEffect(sX, sY, tX, tY));
            count--;
            isDone = count == 0;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {}
    
    @Override
    public void dispose() {}
}
