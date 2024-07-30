package rs.wolf.theastray.vfx.combat;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;
import rs.wolf.theastray.utils.TAImageMst;

public class MagicStarBounceEffectSample extends StarBounceEffect {
    public MagicStarBounceEffectSample(float x, float y) {
        super(x, y);
        ReflectionHacks.setPrivate(this, StarBounceEffect.class, "img", TAImageMst.MAGIC_DAMAGE_STAR);
        color = Color.WHITE.cpy();
        color.a = 0.0F;
    }
}