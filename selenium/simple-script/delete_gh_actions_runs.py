from pathlib import Path
from time import sleep

from environs import env
from selenium.webdriver.common.by import By

from selenium import webdriver

# Selenium script that deletes all GitHub Action runs of a specific workflow

# Read variables from .env file
env.read_env()
# https://github.com/my-org/my-repo/actions/workflows/my-workflow.yaml
github_actions_runs_url = env.str("GITHUB_ACTIONS_RUNS_URL")
browser_profile_path = env.str("BROWSER_PROFILE_PATH", default="/tmp/firefox-profiles/m8w9ecyp.default-release")


def start_browser():
    # Set a profile to persist the login session across runs
    options = webdriver.FirefoxOptions()
    options.add_argument("-profile")
    options.add_argument(browser_profile_path)

    # Ensure the profile directory exists
    Path(browser_profile_path).mkdir(parents=True, exist_ok=True)

    # Set up the Firefox Selenium WebDriver
    driver = webdriver.Firefox(options=options)
    driver.implicitly_wait(0.25)
    return driver


def delete_github_actions_runs(driver):
    # Navigate to the GitHub Actions runs page for the specified workflow
    driver.get(github_actions_runs_url)

    # Manually login if not already logged in
    while not driver.current_url.startswith(github_actions_runs_url):
        print("Please login!")
        sleep(3)

    # Delete each action run
    while True:
        action_runs = driver.find_elements(by=By.CSS_SELECTOR, value='[id^="check_suite_"]')
        if not action_runs:
            break

        action_run = action_runs[0]
        show_options_button = action_run.find_element(by=By.CSS_SELECTOR, value='[aria-label="Show options"]')
        show_options_button.click()

        delete_button = action_run.find_element(by=By.CSS_SELECTOR, value='button[data-show-dialog-id^=delete]')
        delete_button.click()

        confirm_delete_button = driver.find_element(by=By.CSS_SELECTOR, value='dialog[id^="delete-workflow-run"] button[type="submit"]')
        confirm_delete_button.click()

        sleep(1)  # rate limiting


# Run the script
driver = start_browser()
delete_github_actions_runs(driver)
