package rs.wolf.theastray.utils;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class TAImageMst {
    public static Texture MANA;
    public static Texture CAMPFIRE_ENLIGHTEN_BTN;
    
    public static void Initialize() {
        MANA = load("AstrayAssets/images/ui/manalayout/mana.png");
        CAMPFIRE_ENLIGHTEN_BTN = load("AstrayAssets/images/ui/campfire/meditate.png");
    }
    
    private static Texture load(String path) {
        return ImageMaster.loadImage(path);
    }
}