package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.powers.unique.BlossomPower;

public class BlossomPowerPatch {
    @SpirePatch2(clz = EnergyPanel.class, method = "addEnergy")
    public static class GainEnergyPatch {
        @SpirePostfixPatch
        public static void Postfix(int e) {
            AbstractPlayer p = LMSK.Player();
            if (p.hasPower(BlossomPower.ID)) {
                p.powers.stream().filter(po -> po instanceof BlossomPower)
                        .findFirst()
                        .map(po -> (BlossomPower) po)
                        .ifPresent(po -> po.afterEnergyGained(e));
            }
        }
    }
    @SpirePatch2(clz = EnergyPanel.class, method = "useEnergy")
    public static class UseEnergyPatch {
        @SpirePostfixPatch
        public static void Postfix(int e) {
            AbstractPlayer p = LMSK.Player();
            if (p.hasPower(BlossomPower.ID)) {
                p.powers.stream().filter(po -> po instanceof BlossomPower)
                        .findFirst()
                        .map(po -> (BlossomPower) po)
                        .ifPresent(po -> po.afterEnergyLost(e));
            }
        }
    }
}
