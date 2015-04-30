package stock;


import cmd.annotation.Option;
import cmd.annotation.SubCommand;
import cmd.annotation.SubCommands;
import cmd.annotation.Usage;

@Usage("Usage: CMD [option...] <sub command> [sub command option...]")
public class MainCmd {
    @SubCommands(usage = "the available sub commands listed below", metaVar = "<sub command>",
            commands = {
                    @SubCommand(name = "ListSinaName", impl = SinaNameLister.class, usage = "List all stock names and codes form sina"),
                    @SubCommand(name = "PanToToken", impl = EastmoneyDataGraber.class, usage = "Grab needed info from east money web site")
            })
    private SubCmd subCmd;

    @Option(name = "-h", aliases = "--help", usage = "print this help")
    private boolean help;

    public SubCmd getSubCmd() {
        return subCmd;
    }

    public boolean isHelp() {
        return help;
    }
}
