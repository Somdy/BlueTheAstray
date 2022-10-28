package rs.wolf.theastray.vfx.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import rs.lazymankits.abstracts.LMCustomGameEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StarsInNightEffect extends LMCustomGameEffect {
    private static final Range[] X_Range = {new Range(Settings.WIDTH * 0.1F, Settings.WIDTH * 0.33F), 
            new Range(Settings.WIDTH * 0.33F, Settings.WIDTH * 0.66F), new Range(Settings.WIDTH * 0.66F, Settings.WIDTH * 0.95F)};
    private static final Range[] Y_Range = {new Range(Settings.HEIGHT * 0.85F, Settings.HEIGHT * 0.95F), 
            new Range(Settings.HEIGHT * 0.65F, Settings.HEIGHT * 0.85F), new Range(Settings.HEIGHT * 0.5F, Settings.HEIGHT * 0.65F)};
    
    private List<ShiningStarEffect> stars = new ArrayList<>();
    private int lastX = -1;
    private int lastY = -1;
    private int maxStars;
    private int leastStars;
    
    public StarsInNightEffect(int max, int least) {
        this.maxStars = max;
        this.leastStars = least;
    }
    
    @Override
    public void update() {
        List<ShiningStarEffect> removeList = new ArrayList<>();
        stars.forEach(e -> {
            e.update();
            if (e.isDone) removeList.add(e);
        });
        stars.removeAll(removeList);
        if (stars.size() < leastStars) {
            Vector2 pos = rollPosition();
            stars.add(new ShiningStarEffect(pos.x, pos.y));
        } else if (stars.size() < maxStars && MathUtils.randomBoolean(0.25F)) {
            Vector2 pos = rollPosition();
            stars.add(new ShiningStarEffect(pos.x, pos.y));
        }
    }
    
    private Vector2 rollPosition() {
        int pIndex = rollRange(lastX);
        lastX = pIndex;
        float x = rollValue(X_Range[lastX]);
        pIndex = rollRange(lastY);
        lastY = pIndex;
        float y = rollValue(Y_Range[lastY]);
        return new Vector2(x, y);
    }
    
    private int rollRange(int lastIndex) {
        List<Integer> tmpIndex = new ArrayList<Integer>(){{add(0); add(1); add(2);}};
        tmpIndex.remove(Integer.valueOf(lastIndex));
        return tmpIndex.get(MathUtils.random(tmpIndex.size() - 1));
    }
    
    private float rollValue(Range range) {
        return MathUtils.random(range.min, range.max);
    }
    
    @Override
    public void render(SpriteBatch sb) {
        stars.forEach(e -> e.render(sb));
    }
    
    @Override
    public void dispose() {}
    
    private static class Range {
        private final float min;
        private final float max;
        
        private Range(float min, float max) {
            this.min = min;
            this.max = max;
        }
    }
}