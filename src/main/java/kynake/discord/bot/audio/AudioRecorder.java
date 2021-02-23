package kynake.discord.bot.audio;

// JDA
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.OpusPacket;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

// Java
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioRecorder implements AudioReceiveHandler {
  private Map<Long, FileOutputStream> userRawPCMData;
  private JDA jda;

  public AudioRecorder(JDA jda) {
    this.userRawPCMData = new HashMap<>();
    this.jda = jda;
  }

  // Receiving
  @Override
  public boolean canReceiveUser() {
    return false;
  }

  @Override
  public boolean canReceiveCombined() {
    return false;
  }

  @Override
  public boolean canReceiveEncoded() {
    return true;
  }

  @Override
  public void handleUserAudio(UserAudio userAudio) {
    User user = userAudio.getUser();
    Long userID = user.getIdLong();
    byte[] pcmData = userAudio.getAudioData(1.0f);

    try {
      FileOutputStream fileHandler = userRawPCMData.get(userID);
      if(fileHandler == null) {
        fileHandler = OpenUserPCMFile(user);
        userRawPCMData.put(userID, fileHandler);
      }

      fileHandler.write(pcmData);

    } catch(IOException e) {
      System.err.println("Error writing PCM to file");
      e.printStackTrace();
    }
  }

  @Override
  public void handleCombinedAudio(CombinedAudio combinedAudio) {
    for (Map.Entry<Long, FileOutputStream> userFileEntry : userRawPCMData.entrySet()) {
      // User was not speaking during this period of time
      if(!combinedAudio.getUsers().stream().anyMatch(user -> user.getIdLong() == userFileEntry.getKey())) {
        try {
          userFileEntry.getValue().write(combinedAudio.getAudioData(1.0f));
        } catch (Exception e) {
          System.err.println("Could not record silence for user " + userFileEntry.getKey());
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void handleEncodedAudio(OpusPacket packet) {
    Long userID = packet.getUserId();
    byte[] pcmData = null;
    try {
      pcmData = packet.getAudioData(1.0f);
    } catch(IllegalStateException e) {
      // Sometimes the encoded packets are received out-of-order, due to the fact that audio is sent over UDP
      // In those cases, we simply ignore the audio handling, and wait for the JDA audio handler to sort itself out,
      // The audio we want will be available later
      return;
    }

    try {
      FileOutputStream fileHandler = userRawPCMData.get(userID);
      if(fileHandler == null) {
        fileHandler = OpenUserPCMFile(jda.getUserById(userID));
        userRawPCMData.put(userID, fileHandler);
      }

      fileHandler.write(pcmData);
    } catch(IOException e) {
      System.err.println("Error writing PCM to file");
      e.printStackTrace();
    }
  }

  private FileOutputStream OpenUserPCMFile(User user) throws IOException {
    String filename = userPCMFilename(user);

    try {
      return new FileOutputStream(filename);
    } catch(FileNotFoundException e) {
      System.err.println("Could not create PCM file");
      e.printStackTrace();
      throw new IOException("Could not create PCM file");
    }
  }

  private String userPCMFilename(User user) {
    return user.getName() + ".pcm";
  }

  private String userWAVFilename(User user) {
    return user.getName() + ".wav";
  }

  private void writePCMtoWAV() {
    for (Map.Entry<Long, FileOutputStream> fileEntry : userRawPCMData.entrySet()) {
      try {
        // Close PCM Writing stream
        fileEntry.getValue().close();

        // Open PCM for Reading and WAV for Writing
        User user = jda.getUserById(fileEntry.getKey());

        String pcmFilename = userPCMFilename(user);
        String wavFilename = userWAVFilename(user);

        FileInputStream pcmFile = new FileInputStream(pcmFilename);
        FileOutputStream wavFile = new FileOutputStream(wavFilename);

        AudioInputStream audioStream = new AudioInputStream(pcmFile, AudioReceiveHandler.OUTPUT_FORMAT, pcmFile.available());
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);

        // Close streams
        pcmFile.close();
        wavFile.close();
      } catch(IOException e) {
        e.printStackTrace();
      } finally {

      }

    }
  }

  public void shutdown() {
    writePCMtoWAV();
  }
}
