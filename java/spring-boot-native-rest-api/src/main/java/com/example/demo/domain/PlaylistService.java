package com.example.demo.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaylistService {

  private final PlaylistRepository playlistRepository;

  public Playlist getOrCreatePlaylistByUserId(String userId) {
    return playlistRepository
        .findByUserId(userId)
        .orElseGet(
            () -> playlistRepository.save(Playlist.builder().userId(userId).build()));
  }

  public Playlist addSongToPlaylist(String userId, String songTitle, String songArtist) {
    var playlist = getOrCreatePlaylistByUserId(userId);
    playlist.addSong(songTitle, songArtist);
    return playlistRepository.save(playlist);
  }

  public Playlist removeSongFromPlaylist(String userId, String songId) {
    var playlist = getOrCreatePlaylistByUserId(userId);
    playlist.removeSong(songId);
    return playlistRepository.save(playlist);
  }
}
