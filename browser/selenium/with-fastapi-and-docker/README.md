# Spotify Playlist Service

This service retrieves a list of current songs from the Spotify playlist **Hot Hits Germany** from the Spotify website.

This is a simple example of how to remotely control a web browser using Selenium and Docker.

## How to Run

* Ensure Python 3, Docker and Docker Compose are installed on your system.
* Run `pip install -r requirements.txt` to install all Python libraries.
* Run `docker compose up` to start a Docker container, which runs a Chrome browser and a Selenium WebDriver.
* Run `python -m uvicorn app:app --port 8000 --reload` to start the Service.
* Execute HTTP request `curl -X GET http://localhost:8000/songs` to retrieve the current entries of the Spotify
  playlist.

The screen of the remotely controlled browser can be
viewed [here](http://localhost:7900/?autoconnect=1&resize=scale&password=secret) when the Selenium Docker container is
running.

## How to Build

Execute `docker build -t spotify-service .` to build a Docker image.

Execute `docker run --rm --shm-size="2g" -p 8000:8000 -it spotify-service` to run the Docker image.
