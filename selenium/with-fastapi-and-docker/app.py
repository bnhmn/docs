from textwrap import dedent

import uvicorn
from fastapi import FastAPI

import spotify

app = FastAPI(
    title="Spotify Playlist Service",
    description=dedent(
        """
        This service retrieves a list of current songs from the 
        Spotify playlist **Hot Hits Germany** from the Spotify website
        """
    ),
)


@app.get("/songs")
def get_songs() -> list[spotify.Song]:
    return spotify.get_songs()


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
