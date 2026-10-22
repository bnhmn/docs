package com.example.demo.domain;

import java.util.Optional;

import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/** A playlist repository mock with reasonable defaults. */
public class PlaylistRepositoryMock {

  private final PlaylistRepository repository = Mockito.mock(PlaylistRepository.class);

  public PlaylistRepositoryMock() {
    lenient().when(repository.findByUserId(anyString())).thenReturn(Optional.empty());
    lenient()
        .when(repository.save(any(Playlist.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
  }

  public PlaylistRepositoryMock containing(Playlist playlist) {
    when(repository.findByUserId(playlist.getUserId())).thenReturn(Optional.of(playlist));
    return this;
  }

  PlaylistRepository repository() {
    return repository;
  }
}
