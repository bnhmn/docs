package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A user's playlist containing songs in a defined order. */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

  @Id
  @Builder.Default
  private String id = UUID.randomUUID().toString();

  @NotNull
  @NotBlank
  private String userId;

  @NotNull
  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("position ASC")
  @Builder.Default
  private List<@NotNull PlaylistSong> songs = new ArrayList<>();

  /** Add a song to the end of the playlist. */
  public void addSong(String title, String artist) {
    songs.add(PlaylistSong.builder()
        .playlist(this)
        .position(getNextPosition())
        .title(title)
        .artist(artist)
        .build());
  }

  /** Remove the song with the given ID from the playlist if it exists. */
  public void removeSong(String songId) {
    songs.removeIf(song -> song.getId().equals(songId));
  }

  private int getNextPosition() {
    var lastSongPos = songs.stream().mapToInt(PlaylistSong::getPosition).max().orElse(-1);
    return lastSongPos + 1;
  }
}
