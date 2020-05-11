package be.renaud11232.plugins.sqleconomy.database.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class ReloadTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = Collections.singletonList("reload");
        if(strings.length == 1 && !strings[0].equals("reload") && "reload".startsWith(strings[0])) {
            return completions;
        }
        return Collections.emptyList();
    }

}
