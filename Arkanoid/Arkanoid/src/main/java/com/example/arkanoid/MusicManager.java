package com.example.arkanoid;

import com.almasb.fxgl.audio.Audio;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private static MusicManager instance;
    private Map<String, MediaPlayer> musicPlayers;
    private Map<String, AudioClip> soundEffects;
    private MediaPlayer currentMusic;

    private double musicVolume = 1.0;
    private double soundVolume = 0.7;
    private boolean musicEnabled = true;

    private MusicManager() {
        musicPlayers = new HashMap<>();
        soundEffects = new HashMap<>();
        loadAudio();
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    private void loadAudio() {
        try {
            loadMusic("gameplay", "Dreamscape Drift.mp3");
            loadMusic("menu", "Hedwig's Theme.mp3");

            loadSoundEffect("button_toggle", "button_toggle.mp3");
            loadSoundEffect("collide", "brick_break.mp3");
        } catch (Exception e) {
            System.err.println("Error loading audio");
            e.printStackTrace();
        }
    }

    private void loadMusic(String type, String fileName) {
        try {
            URL resource = getClass().getResource("music/" + fileName);
            if (resource == null) {
                System.err.println("Can't find file" + fileName);
                return;
            }
            Media media = new Media(resource.toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(musicVolume);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayers.put(type, player);
        } catch (Exception e) {
            System.err.println("Can't load music file");
            e.printStackTrace();
        }
    }
    private void loadSoundEffect(String type, String fileName) {
        try {
            URL resource = getClass().getResource("sound/" + fileName);
            if (resource == null) {
                System.err.println("Can't find file"+fileName);
                return;
            }
            AudioClip clip = new AudioClip(resource.toString());
            clip.setVolume(soundVolume);
            soundEffects.put(type, clip);
        } catch (Exception e) {
            System.err.println("Can't load sound file");
        }

    }
    public void playMusic(String musicName) {
        if (currentMusic != null) {
            currentMusic.stop();
        }
        MediaPlayer player = musicPlayers.get(musicName);
        if (player != null) {
            player.setVolume(musicVolume);
            player.seek(Duration.ZERO);
            player.play();
            currentMusic = player;
            System.out.println("Playing music " + musicName);
        } else {
            System.err.println("Can't play music " + musicName);
        }
    }

    public void playSoundEffect(String soundName) {
        AudioClip player = soundEffects.get(soundName);
        if(player != null) {
            player.setVolume(soundVolume);
            player.play();
        } else {
            System.err.println("Can't play sound " + soundName);
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }

    public void setVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);
        }
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getSoundVolume() {
        return soundVolume;
    }
}
