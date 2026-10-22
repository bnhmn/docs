package com.example.demo.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface PlaylistRepository extends JpaRepository<Playlist, Long> {

  Optional<Playlist> findByUserId(String userId);
}
