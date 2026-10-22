package com.example.demo.domain;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A song entry in a playlist, including its title, artist, and position. */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSong {

  @Id
  @Builder.Default
  private String id = UUID.randomUUID().toString();

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id")
  private Playlist playlist;

  @NotNull
  @PositiveOrZero
  private Integer position;

  @NotNull
  @NotBlank
  private String title;

  @NotNull
  @NotBlank
  private String artist;
}
