package rs.wolf.theastray.ui.energyOrb;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.wolf.theastray.utils.TAUtils;

public class EnergyOrbTheAstray extends CustomEnergyOrb implements TAUtils {
    private static final int ORB_W = 128;
    private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
    public static String VFX_PATH = "AstrayAssets/images/ui/orb/vfx.png";
    private float angle2;
    private float angle3;
    private float angle4;
    
    public EnergyOrbTheAstray() {
        super(null, VFX_PATH, null);
        energyLayers = new Texture[6];
        energyLayers[0] = ImageMaster.loadImage("AstrayAssets/images/ui/orb/1.png");
        energyLayers[1] = ImageMaster.loadImage("AstrayAssets/images/ui/orb/2.png");
        energyLayers[2] = ImageMaster.loadImage("AstrayAssets/images/ui/orb/3.png");
        energyLayers[3] = ImageMaster.loadImage("AstrayAssets/images/ui/orb/4.png");
        energyLayers[4] = ImageMaster.loadImage("AstrayAssets/images/ui/orb/5.png");
        energyLayers[5] = ImageMaster.loadImage("AstrayAssets/images/ui/orb/6.png");
    }
    
    @Override
    public void updateOrb(int energyCount) {
        if (energyCount > 0) {
            angle2 += Gdx.graphics.getDeltaTime() * 20F;
            angle3 += Gdx.graphics.getDeltaTime() * -20F;
            angle4 += Gdx.graphics.getDeltaTime() * 15F;
            if (angle3 < -360F) angle3 += 360F;
            if (angle2 > 360F) angle2 -= 360F;
        } else {
            angle4 += Gdx.graphics.getDeltaTime() * 10F;
            if (angle2 % 120 != 0 && Math.abs(angle2 % 120) > 1F) {
                angle2 += Gdx.graphics.getDeltaTime() * (Math.abs(angle2 % 120) / 10F);
            }
            if (angle3 % 120 != 0 && Math.abs(angle3 % 120) > 1F) {
                angle3 -= Gdx.graphics.getDeltaTime() * (Math.abs(angle3 % 120) / 10F);
            }
        }
    }
    
    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        sb.setColor(Color.WHITE);
        sb.draw(energyLayers[0], current_x - 64F, current_y - 64F, 64F, 64F, ORB_W, ORB_W, 
                ORB_IMG_SCALE, ORB_IMG_SCALE, angle2, 0, 0, ORB_W, ORB_W, false, false);
        sb.draw(energyLayers[1], current_x - 64F, current_y - 64F, 64F, 64F, ORB_W, ORB_W, 
                ORB_IMG_SCALE, ORB_IMG_SCALE, angle3, 0, 0, ORB_W, ORB_W, false, false);
        if (enabled) {
            sb.draw(energyLayers[2], current_x - 64F, current_y - 64F, 64F, 64F, ORB_W, ORB_W,
                    ORB_IMG_SCALE, ORB_IMG_SCALE, 0F, 0, 0, ORB_W, ORB_W, false, false);
        } else {
            sb.draw(energyLayers[3], current_x - 64F, current_y - 64F, 64F, 64F, ORB_W, ORB_W,
                    ORB_IMG_SCALE, ORB_IMG_SCALE, 0F, 0, 0, ORB_W, ORB_W, false, false);
            sb.draw(energyLayers[4], current_x - 64F, current_y - 64F, 64F, 64F, ORB_W, ORB_W,
                    ORB_IMG_SCALE, ORB_IMG_SCALE, 0F, 0, 0, ORB_W, ORB_W, false, false);
        }
        sb.draw(energyLayers[5], current_x - 64F, current_y - 64F, 64F, 64F, ORB_W, ORB_W,
                ORB_IMG_SCALE, ORB_IMG_SCALE, angle4, 0, 0, ORB_W, ORB_W, false, false);
//        sb.setBlendFunction(770, 1);
//        sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);
    }
}
