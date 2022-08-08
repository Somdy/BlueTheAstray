package rs.wolf.theastray.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class CheatCMD extends ConsoleCommand {
    @Override
    protected void execute(String[] tokens, int i) {
        if (tokens.length < 1) {
            cmdEnergyHelp();
            return;
        }
        if (tokens[1].equalsIgnoreCase("Enlightened") && tokens.length > 2) {
            boolean enabled = Boolean.parseBoolean(tokens[2]);
            Cheat.CHEATS[Cheat.IEL] = enabled;
            DevConsole.log("Enlightened: " + Cheat.IsCheating(Cheat.IEL));
        } else {
            cmdEnergyHelp();
        }
    }
    
    @Override
    protected void errorMsg() {
        cmdEnergyHelp();
    }
    
    private static void cmdEnergyHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("If you don't know how to cheat, don't cheat.");
    }
}