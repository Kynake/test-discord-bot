package kynake.discord.bot;

import kynake.discord.bot.commands.*;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
    String[] args = event.getMessage().getContentDisplay().split("\\s+");

    if(args.length > 0 && args[0].startsWith(prefix)) {
      Command command = commands.get(args[0].substring(prefix.length()).toLowerCase());
      if(command != null) {
        command.execute(event, Arrays.copyOfRange(args, 1, args.length));
      }
    }
  }
}
