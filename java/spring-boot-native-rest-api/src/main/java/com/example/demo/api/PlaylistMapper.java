package com.example.demo.api;

import org.mapstruct.Mapper;

import com.example.demo.domain.Playlist;

@Mapper
public interface PlaylistMapper {

  RestPlaylist toRest(Playlist playlist);
}
