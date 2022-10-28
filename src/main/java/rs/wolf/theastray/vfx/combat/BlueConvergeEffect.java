package rs.wolf.theastray.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.lazymankits.abstracts.LMCustomGameEffect;

public class BlueConvergeEffect extends LMCustomGameEffect {
    private float destX;
    private float destY;
    private float radius;
    private float timer;
    private float rotation;
    private int count;
    
    public BlueConvergeEffect(int count, float destX, float destY, float radius) {
        this.count = count;
        this.destX = destX;
        this.destY = destY;
        this.radius = radius;
    }
    
    @Override
    public void update() {
        timer -= Gdx.graphics.getDeltaTime();
        if (timer < 0F) {
            count--;
            timer = MathUtils.random(0F, 0.02F);
            int iCount = MathUtils.random(3, 5);
            for (int i = 0; i < iCount; i++) {
                rotation = MathUtils.random(360F);
                Vector2 start = new Vector2(destX + radius, destY + radius);
                start.setAngle(rotation);
                AbstractDungeon.effectsQueue.add(new LightOrbFlyInEffect(start.x, start.y, destX, destY, 
                        new Color(MathUtils.random(0.3F, 1.0F), 0.3F, MathUtils.random(0.6F, 1.0F), 1F)));
            }
        }
        if (count <= 0) isDone = true;
    }
    
    @Override
    public void render(SpriteBatch sb) {}
    
    @Override
    public void dispose() {}
}