package com.example.demo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.security.CurrentUser;
import com.example.demo.domain.PlaylistService;

@RestController
@RequiredArgsConstructor
public class PlaylistController implements PlaylistApi {

  private final CurrentUser currentUser;
  private final PlaylistService playlistService;
  private final PlaylistMapper playlistMapper;

  @Override
  public ResponseEntity<RestPlaylist> getMyPlaylist() {
    var userId = currentUser.getUserId();
    var playlist = playlistService.getOrCreatePlaylistByUserId(userId);
    var response = playlistMapper.toRest(playlist);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<RestPlaylist> addSongToMyPlaylist(RestNewSong song) {
    var userId = currentUser.getUserId();
    var playlist = playlistService.addSongToPlaylist(userId, song.getTitle(), song.getArtist());
    var response = playlistMapper.toRest(playlist);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<RestPlaylist> removeSongFromMyPlaylist(String songId) {
    var userId = currentUser.getUserId();
    var playlist = playlistService.removeSongFromPlaylist(userId, songId);
    var response = playlistMapper.toRest(playlist);
    return ResponseEntity.ok(response);
  }
}
