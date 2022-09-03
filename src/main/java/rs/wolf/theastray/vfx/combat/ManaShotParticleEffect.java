package rs.wolf.theastray.vfx.combat;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.combat.BloodShotParticleEffect;
import rs.lazymankits.utils.LMSK;

public class ManaShotParticleEffect extends BloodShotParticleEffect {
    public ManaShotParticleEffect(float sX, float sY, float tX, float tY) {
        super(sX, sY, tX, tY);
        color = LMSK.Color(0, MathUtils.random(180, 200), MathUtils.random(230, 250));
    }
}
