package rs.wolf.theastray.vfx.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class MagicDamageEffect extends AbstractGameEffect {
    
    private float x;
    private float y;
    private int extraStars;
    
    public MagicDamageEffect(float x, float y) {
        this(x, y, 0);
    }
    
    public MagicDamageEffect(float x, float y, int unblockedDamage) {
        this.x = x;
        this.y = y;
        this.extraStars = unblockedDamage;
        if (this.extraStars > 20)
            this.extraStars = 20;
    }
    
    @Override
    public void update() {
        isDone = true;
        int stars = 5 + extraStars;
        for (int i = 0; i < stars; i++) {
            AbstractDungeon.effectsQueue.add(new MagicStarBounceEffectSample(x, y));
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        
    }
    
    @Override
    public void dispose() {
        
    }
}
