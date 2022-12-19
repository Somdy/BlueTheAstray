package rs.wolf.theastray.utils;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class TAImageMst {
    public static Texture MANA;
    public static Texture CAMPFIRE_ENLIGHTEN_BTN;
    public static Texture TINY_STAR;
    public static Texture TINY_STAR_GLOW_MASK;
    
    public static void Initialize() {
        MANA = load("AstrayAssets/images/ui/manalayout/mana.png");
        CAMPFIRE_ENLIGHTEN_BTN = load("AstrayAssets/images/ui/campfire/meditate.png");
        TINY_STAR = load("AstrayAssets/images/vfx/tinyStar.png");
        TINY_STAR_GLOW_MASK = load("AstrayAssets/images/vfx/tinyStar_glowMask.png");
    }
    
    private static Texture load(String path) {
        return ImageMaster.loadImage(path);
    }
}