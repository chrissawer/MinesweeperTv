import unittest
from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.webdriver.extensions.android.nativekey import AndroidKey
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# {"platformName": "Android", "automationName": "uiautomator2", "deviceName": "Android", "appPackage": "uk.co.whitewillow.minesweepertv", "appActivity": ".MainActivity"}
capabilities = dict(
    platformName='Android',
    automationName='uiautomator2',
    deviceName='Android',
    appPackage='uk.co.whitewillow.minesweepertv',
    appActivity='.MainActivity'
)

class TestMars(unittest.TestCase):
    def setUp(self) -> None:
        self.driver = webdriver.Remote('http://localhost:4723', capabilities)

    def tearDown(self) -> None:
        if self.driver:
            self.driver.quit()

    def testGamesStateIsStopped(self) -> None:
        currentGameStateElement = self.driver.find_element(AppiumBy.XPATH, '//*[@resource-id="currentGameState"]')
        self.assertEqual("STOPPED", currentGameStateElement.text)

        self.driver.press_keycode(AndroidKey.DPAD_DOWN)
        self.assertEqual(currentGameStateElement, self.driver.find_element(AppiumBy.XPATH, '//*[@focused="true"]'))

if __name__ == '__main__':
    unittest.main()
