package com.example.demo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static com.example.demo.fixtures.PlaylistFixtures.*;

class PlaylistTest {

  @Test
  void addsSongsAtIncreasingPositionsAndLinksThemToPlaylist() {
    var playlist = aPlaylist();

    playlist.addSong(SONG_TITLE_1, ARTIST_1);
    playlist.addSong(SONG_TITLE_2, ARTIST_2);

    assertThat(playlist.getSongs())
        .extracting(PlaylistSong::getTitle)
        .containsExactly(SONG_TITLE_1, SONG_TITLE_2);
    assertThat(playlist.getSongs()).extracting(PlaylistSong::getPosition).containsExactly(0, 1);
    assertThat(playlist.getSongs())
        .allSatisfy(song -> assertThat(song.getPlaylist()).isSameAs(playlist));
  }

  @Test
  void removesMatchingSong() {
    var playlist = aPlaylist();
    playlist.addSong(SONG_TITLE_1, ARTIST_1);
    playlist.addSong(SONG_TITLE_2, ARTIST_2);

    playlist.removeSong(playlist.getSongs().getFirst().getId());

    assertThat(playlist.getSongs())
        .extracting(PlaylistSong::getTitle)
        .containsExactly(SONG_TITLE_2);
  }

  @Test
  void removesNoSongIfIdIsUnknown() {
    var playlist = aPlaylist();
    playlist.addSong(SONG_TITLE_1, ARTIST_1);
    playlist.addSong(SONG_TITLE_2, ARTIST_2);

    playlist.removeSong(UNKNOWN_SONG_ID);

    assertThat(playlist.getSongs())
        .extracting(PlaylistSong::getTitle)
        .containsExactly(SONG_TITLE_1, SONG_TITLE_2);
  }

  @Test
  void nextSongUsesPositionAfterCurrentMaximumAfterRemoval() {
    var playlist = aPlaylist();
    playlist.addSong(SONG_TITLE_1, ARTIST_1);
    playlist.addSong(SONG_TITLE_2, ARTIST_2);
    playlist.addSong(SONG_TITLE_3, ARTIST_3);
    playlist.removeSong(playlist.getSongs().get(2).getId());

    playlist.addSong(SONG_TITLE_4, ARTIST_4);

    assertThat(playlist.getSongs())
        .extracting(PlaylistSong::getTitle)
        .containsExactly(SONG_TITLE_1, SONG_TITLE_2, SONG_TITLE_4);
    assertThat(playlist.getSongs()).extracting(PlaylistSong::getPosition).containsExactly(0, 1, 2);
  }
}
