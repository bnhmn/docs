from os import environ

from pydantic import BaseModel, Field
from selenium import webdriver
from selenium.common import WebDriverException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.expected_conditions import element_to_be_clickable
from selenium.webdriver.support.wait import WebDriverWait

HEADLESS_MODE = bool(environ.get("HEADLESS_MODE", "false").lower() == "true")
TIMEOUT_SECONDS = int(environ.get("TIMEOUT_SECONDS", "10"))


class Song(BaseModel):
    name: str = Field(examples=["Rolling In the Deep", "Just Can't Get Enough"])


def get_songs() -> list[Song]:
    driver = create_webdriver()
    wait = WebDriverWait(driver, timeout=TIMEOUT_SECONDS)

    try:
        # View Spotify Playlist "Hot Hits Germany"
        driver.get("https://open.spotify.com/playlist/37i9dQZF1DX4jP4eebSWR9")

        # Accept cookies
        cookies_button = wait.until(element_to_be_clickable((By.ID, "onetrust-accept-btn-handler")))
        cookies_button.click()

        # Read songs
        songs = driver.find_elements(By.CSS_SELECTOR, 'a[data-testid="internal-track-link"]')
        songs = [Song(name=song.text) for song in songs]
        return songs

    except WebDriverException:
        raise RuntimeError("Could not retrieve playlist from Spotify")

    finally:
        driver.quit()


def create_webdriver():
    options = webdriver.ChromeOptions()
    if HEADLESS_MODE:
        options.add_argument("--headless=chrome")

    return webdriver.Remote(options=options)
