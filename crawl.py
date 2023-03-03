from datetime import timedelta
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.chrome.webdriver import WebDriver
from datetime import datetime


def wait_driver(_driver: WebDriver, by: By, value):
    WebDriverWait(_driver, 100).until(EC.presence_of_element_located((by, value)))


def wait_and_find(_driver: WebDriver, by: By, value: str, multiple=False):
    wait_driver(_driver, by, value)
    if multiple:
        return _driver.find_elements(by, value)
    else:
        return _driver.find_element(by, value)


def scroll_down_while(target: WebElement, while_seconds):
    start = datetime.now()
    end = start + timedelta(seconds=while_seconds)
    target.click()
    while True:
        target.send_keys(Keys.PAGE_DOWN)
        if datetime.now() > end:
            break


def print_page(target):
    store_list = target.find_elements(By.CSS_SELECTOR, "ul > li")
    for store in store_list:
        switch_to("searchIframe")
        store_name = store.find_element(By.CSS_SELECTOR,
                                        "div > span:nth-child(1)")
        store_name.click()
        print(f"clicked {store_name.text}")


        switch_to("entryIframe")
        inner_spans = wait_and_find(driver, By.CSS_SELECTOR, "#_title > span", multiple=True)
        for inner_span in inner_spans:
            print(inner_span.text)
        print()





def switch_to(frame: str):
    driver.switch_to.default_content()
    WebDriverWait(driver, 100).until(EC.frame_to_be_available_and_switch_to_it(
        (By.CSS_SELECTOR, "#"+frame)))
    # driver.switch_to.frame(frame)


def next_page():
    cand = wait_and_find(driver, By.CLASS_NAME, "place_blind")
    for c in cand:
        if c.text == "다음페이지":
            t = c.find_element(By.XPATH, "..")
            t.click()
            break


if __name__ == "__main__":
    driver = webdriver.Chrome()
    main_query = "음식점"
    driver.get(f"https://map.naver.com/v5/search/{main_query}")


    switch_to("searchIframe")
    scroll_container = wait_and_find(driver,
                                     By.ID,
                                    "_pcmap_list_scroll_container")

    while True:
        driver.implicitly_wait(10)
        body = wait_and_find(driver, By.CSS_SELECTOR, "body")
        scroll_down_while(body, 20)
        print_page(scroll_container)
        switch_to("searchIframe")
        next_page()
    driver.close()
