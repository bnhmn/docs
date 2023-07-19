import os
from time import sleep

from selenium import webdriver
from selenium.webdriver.common.by import By

# Selenium script that automatically presses the 'Approve' button on GitHub Actions deployments

options = webdriver.FirefoxOptions()
options.add_argument("-profile")
options.add_argument("/tmp/firefox-profiles/m8w9ecyp.default-release")

driver = webdriver.Firefox(options=options)
driver.implicitly_wait(0.25)

driver.get("https://github.com/ben5311/ranger2/actions")

while not driver.title.startswith("Actions"):
    print("Please login!") # TODO: add automatic login here
    sleep(3)

action_runs = driver.find_elements(by=By.CSS_SELECTOR, value='[id^="check_suite_"]')
waiting_runs = [run for run in action_runs if "Waiting" in run.text]
links = [run.find_element(by=By.TAG_NAME, value="a").get_attribute("href") for run in waiting_runs]

for link in links:
    driver.get(link)
    buttons = driver.find_elements(by=By.CSS_SELECTOR, value="button[id^=dialog-show]")
    buttons = [button for button in buttons if "Review deployments" in button.text]
    button = buttons[0]
    button.click()
    checkboxes = driver.find_elements(by=By.CSS_SELECTOR,
                                      value='input[type="checkbox"][name="gate_request[]"]')
    for checkbox in checkboxes:
        if checkbox.size["height"] > 0:
            checkbox.click()
    submit_buttons = driver.find_elements(By.CSS_SELECTOR,
                                          'button[type="submit"][value="approved"]')
    for button in submit_buttons:
        if button.size["height"] > 0:
            button.click()
    sleep(1)

# driver.quit() # Don't quit so the Browser window stays open when finished
