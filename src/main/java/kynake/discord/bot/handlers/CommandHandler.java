package kynake.discord.bot.handlers;

// Internal
import kynake.discord.bot.commands.*;

// JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// Java
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashMap;

public class CommandHandler extends ListenerAdapter {
  public static String prefix = "!";

  private Map<String, Command> commands;

  public CommandHandler() {
    commands = new HashMap<String, Command>();

    // Register Commands
    createCommand(Info.class);
    createCommand(Shutdown.class);
    createCommand(JoinVoice.class);

    System.out.println("Created Commands: " + commands.keySet().stream().map(command -> prefix + command).collect(Collectors.joining(", ")));
  }

  private void createCommand(Class<? extends Command> commandClass) {
    try {
      Command commandObject = commandClass.newInstance();
      if(commands.putIfAbsent(commandObject.register(), commandObject) != null) {
        System.err.println(commandObject.getCommandString() + " has already been registered!");
        return;
      }

    } catch(IllegalAccessException | InstantiationException | ExceptionInInitializerError | SecurityException e) {
      System.err.println("Could not instantiate command " + commandClass.getName());
    }
  }

  public Map<String, Command> getCommands() {
    return this.commands;
  }

  public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    String message = event.getMessage().getContentDisplay();

    // Only parse messages that start with the prefix
    if(message.length() == 0 || !message.startsWith(prefix)) {
      return;
    }

    String[] args = message.split("\\s+");
    Command command = commands.get(args[0].substring(prefix.length()).toLowerCase());
    if(command != null) {
      command.execute(event, Arrays.copyOfRange(args, 1, args.length));
    }
  }
}
