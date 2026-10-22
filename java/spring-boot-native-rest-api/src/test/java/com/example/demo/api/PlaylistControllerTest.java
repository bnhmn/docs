package com.example.demo.api;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.example.demo.IntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.example.demo.fixtures.PlaylistFixtures.*;

@IntegrationTest
class PlaylistControllerTest {

  @Autowired
  private RestTestClient authorizedTestClient;

  @Test
  void getsAndCreatesEmptyPlaylistForAuthenticatedUser() {
    var playlist = getPlaylist();

    assertThat(playlist.getSongs()).isEmpty();
  }

  @Test
  void addsSongsAndReturnsThemInPlaylistOrder() {
    var playlist = addSong(aNewSong(SONG_TITLE_1, ARTIST_1), aNewSong(SONG_TITLE_2, ARTIST_2));

    assertThat(playlist.getSongs()).hasSize(2);
    assertThat(playlist.getSongs())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(List.of(aSong(SONG_TITLE_1, ARTIST_1), aSong(SONG_TITLE_2, ARTIST_2)));
  }

  @Test
  void deletesSongAndReturnsUpdatedPlaylist() {
    var playlist = addSong(aNewSong(SONG_TITLE_1, ARTIST_1), aNewSong(SONG_TITLE_2, ARTIST_2));
    var songId = playlist.getSongs().getFirst().getId();

    var result = authorizedTestClient
        .delete()
        .uri("/me/playlist/songs/{songId}", songId)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(RestPlaylist.class)
        .returnResult()
        .getResponseBody();

    assertThat(result).isNotNull();
    assertThat(result.getSongs())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(List.of(aSong(SONG_TITLE_2, ARTIST_2)));
  }

  @Test
  void deletingSongThatIsNotInPlaylistSucceedsAndLeavesPlaylistUnchanged() {
    var playlist = addSong(aNewSong(SONG_TITLE_1, ARTIST_1));

    var result = authorizedTestClient
        .delete()
        .uri("/me/playlist/songs/{songId}", UNKNOWN_SONG_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(RestPlaylist.class)
        .returnResult()
        .getResponseBody();

    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(playlist);
  }

  @ParameterizedTest
  @MethodSource("invalidSongs")
  void rejectsSongRequestsMissingRequiredFields(RestNewSong newSong) {
    authorizedTestClient
        .post()
        .uri("/me/playlist/songs")
        .contentType(MediaType.APPLICATION_JSON)
        .body(newSong)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  private static Stream<Arguments> invalidSongs() {
    return Stream.of(arguments(aNewSong(SONG_TITLE_1, null)), arguments(aNewSong(null, ARTIST_1)));
  }

  private RestPlaylist getPlaylist() {
    var playlist = authorizedTestClient
        .get()
        .uri("/me/playlist")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(RestPlaylist.class)
        .returnResult()
        .getResponseBody();
    assertThat(playlist).isNotNull();
    return playlist;
  }

  private RestPlaylist addSong(RestNewSong... newSongs) {
    RestPlaylist playlist = null;
    for (var newSong : newSongs) {
      playlist = authorizedTestClient
          .post()
          .uri("/me/playlist/songs")
          .contentType(MediaType.APPLICATION_JSON)
          .body(newSong)
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody(RestPlaylist.class)
          .returnResult()
          .getResponseBody();
    }
    assertThat(playlist).isNotNull();
    return playlist;
  }
}
