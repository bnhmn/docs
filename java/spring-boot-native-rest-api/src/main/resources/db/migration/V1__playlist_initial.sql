CREATE TABLE playlist
(
    id      TEXT PRIMARY KEY,
    user_id TEXT NOT NULL UNIQUE
);

CREATE TABLE playlist_song
(
    id          TEXT PRIMARY KEY,
    playlist_id TEXT    NOT NULL REFERENCES playlist (id) ON DELETE CASCADE,
    position    INTEGER NOT NULL CHECK (position >= 0),
    title       TEXT    NOT NULL,
    artist TEXT NOT NULL,
    CONSTRAINT uq_playlist_song_position UNIQUE (playlist_id, position)
);

CREATE INDEX idx_playlist_song_playlist_id ON playlist_song (playlist_id);
