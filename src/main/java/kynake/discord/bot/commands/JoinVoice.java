package kynake.discord.bot.commands;

// JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import kynake.discord.bot.audio.AudioRecorder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinVoice implements Command {
  private String commandString = "voice";

  public String getCommandString() {
    return commandString;
  }

  public String register() {
    System.out.println("JoinVoice registered");
    return getCommandString();
  }

  public boolean execute(GuildMessageReceivedEvent event, String[] args) {
    System.out.println("JoinVoice executed");

    // Get the voice channel the Author of the message is at
    VoiceChannel authorVoiceChannel = event.getMember().getVoiceState().getChannel();
    if(authorVoiceChannel == null) {
      System.out.println("Author is not is a voice channel");
      return false;
    }

    // If Author is in a voice channel in the Guild, join it

    AudioManager audioManager = event.getGuild().getAudioManager();
    AudioRecorder recorder = new AudioRecorder();

    audioManager.setReceivingHandler(recorder);
    audioManager.setSendingHandler(recorder); // Test audio with echo
    audioManager.openAudioConnection(authorVoiceChannel);

    return true;
  }
}
