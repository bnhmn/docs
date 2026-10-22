package com.example.demo.fixtures;

import lombok.experimental.UtilityClass;

import com.example.demo.api.RestNewSong;
import com.example.demo.api.RestSong;
import com.example.demo.domain.Playlist;

import static com.example.demo.fixtures.SecurityFixtures.TEST_USER_1;

/** Shared test data and factories for playlist tests. */
@UtilityClass
public final class PlaylistFixtures {

  public static final String SONG_TITLE_1 = "First Song";
  public static final String SONG_TITLE_2 = "Second Song";
  public static final String SONG_TITLE_3 = "Third Song";
  public static final String SONG_TITLE_4 = "Fourth Song";
  public static final String ARTIST_1 = "First Artist";
  public static final String ARTIST_2 = "Second Artist";
  public static final String ARTIST_3 = "Third Artist";
  public static final String ARTIST_4 = "Fourth Artist";
  public static final String UNKNOWN_SONG_ID = "unknown-song-id";

  public static Playlist aPlaylist() {
    return Playlist.builder().userId(TEST_USER_1).build();
  }

  public static RestNewSong aNewSong(String title, String artist) {
    return RestNewSong.builder().title(title).artist(artist).build();
  }

  public static RestSong aSong(String title, String artist) {
    return RestSong.builder().title(title).artist(artist).build();
  }
}
