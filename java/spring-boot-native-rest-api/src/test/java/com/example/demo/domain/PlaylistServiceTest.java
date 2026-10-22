package com.example.demo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static com.example.demo.fixtures.PlaylistFixtures.ARTIST_1;
import static com.example.demo.fixtures.PlaylistFixtures.SONG_TITLE_1;
import static com.example.demo.fixtures.PlaylistFixtures.aPlaylist;
import static com.example.demo.fixtures.SecurityFixtures.TEST_USER_1;

class PlaylistServiceTest {

  @Test
  void createsAndSavesEmptyPlaylistWhenMissing() {
    var playlistRepo = new PlaylistRepositoryMock();
    var playlistService = new PlaylistService(playlistRepo.repository());

    var playlist = playlistService.getOrCreatePlaylistByUserId(TEST_USER_1);

    assertThat(playlist.getUserId()).isEqualTo(TEST_USER_1);
    assertThat(playlist.getSongs()).isEmpty();
    verify(playlistRepo.repository()).save(playlist);
  }

  @Test
  void returnsExistingPlaylistWithoutSaving() {
    var playlist = aPlaylist();
    var playlistRepo = new PlaylistRepositoryMock().containing(playlist);
    var playlistService = new PlaylistService(playlistRepo.repository());

    assertThat(playlistService.getOrCreatePlaylistByUserId(TEST_USER_1)).isSameAs(playlist);

    verify(playlistRepo.repository(), never()).save(any());
  }

  @Test
  void addsSongAndSavesPlaylist() {
    var playlist = aPlaylist();
    var playlistRepo = new PlaylistRepositoryMock().containing(playlist);
    var playlistService = new PlaylistService(playlistRepo.repository());

    var result = playlistService.addSongToPlaylist(TEST_USER_1, SONG_TITLE_1, ARTIST_1);

    assertThat(result).isSameAs(playlist);
    assertThat(result.getSongs()).singleElement().satisfies(song -> {
      assertThat(song.getTitle()).isEqualTo(SONG_TITLE_1);
      assertThat(song.getArtist()).isEqualTo(ARTIST_1);
    });
    verify(playlistRepo.repository()).save(playlist);
  }

  @Test
  void createsPlaylistBeforeAddingSongWhenMissing() {
    var playlistRepo = new PlaylistRepositoryMock();
    var playlistService = new PlaylistService(playlistRepo.repository());

    var playlist = playlistService.addSongToPlaylist(TEST_USER_1, SONG_TITLE_1, ARTIST_1);

    assertThat(playlist.getSongs()).singleElement().satisfies(song -> {
      assertThat(song.getTitle()).isEqualTo(SONG_TITLE_1);
      assertThat(song.getArtist()).isEqualTo(ARTIST_1);
    });
    verify(playlistRepo.repository(), times(2)).save(playlist);
  }

  @Test
  void removesSongAndSavesPlaylist() {
    var playlist = aPlaylist();
    playlist.addSong(SONG_TITLE_1, ARTIST_1);
    var songId = playlist.getSongs().getFirst().getId();
    var playlistRepo = new PlaylistRepositoryMock().containing(playlist);
    var playlistService = new PlaylistService(playlistRepo.repository());

    var result = playlistService.removeSongFromPlaylist(TEST_USER_1, songId);

    assertThat(result).isSameAs(playlist);
    assertThat(result.getSongs()).isEmpty();
    verify(playlistRepo.repository()).save(playlist);
  }
}
