package kynake.discord.bot.commands;

// Internal
import kynake.discord.bot.audio.AudioTester;
import kynake.discord.bot.audio.AudioRecorder;

// JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
      System.out.println("Author is not in a voice channel");
      return false;
    }

    // If Author is in a voice channel in the Guild, join it

    AudioManager audioManager = event.getGuild().getAudioManager();
    AudioRecorder recorder = new AudioRecorder(event.getAuthor(), "out.wav");

    audioManager.setReceivingHandler(recorder);
    // audioManager.setSendingHandler(recorder); // Test audio with echo
    audioManager.openAudioConnection(authorVoiceChannel);

    return true;
  }
}
