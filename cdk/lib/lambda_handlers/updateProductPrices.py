'''
void updateProductPrices()
- updates products tracked by DynamoDB#1 if last updated over an hour ago
- Python script that webscrapes Amazon with Selenium/BeautifulSoup
'''
from selenium import webdriver
from selenium.webdriver.common.by import By
from datetime import datetime
import time

def webscrapeAmazon(URL_LIST):
    def webscrapeAmazonUrl(URL, retry=3):
        # If out of retries, return None
        if retry == 0:
            return None
        
        # Navigate to Amazon
        driver.get(URL)
        time.sleep(3)
        
        # Attributes to find
        truncated_title = ""
        current_price = None
        original_price = None
        last_updated = None
        
        # Get Product Title and Truncate It
        try:
            title = driver.find_element(By.ID, "productTitle").text
        except:
            return webscrapeAmazonUrl(URL, retry-1)
        title_frags = title.replace(',',':').replace(' -',':').replace('- ',':').split(':')
        for title_frag in title_frags:
            truncated_title += title_frag
            if len(truncated_title.split()) >= 5:
                break
        truncated_title = truncated_title.replace("  "," ")
        
        # Get Product Current Price
        try:
            current_price = driver.find_elements(By.XPATH, '//span[@class="a-price aok-align-center reinventPricePriceToPayMargin priceToPay"]/span[@class="a-offscreen"]')[0].get_attribute('textContent')
        except:
            return webscrapeAmazonUrl(URL, retry-1)
        
        # Get Product Original Price
        try:
            original_price = driver.find_elements(By.XPATH, '//span[@data-a-strike="true" and @class="a-price a-text-price" and @data-a-color="secondary"]/span[@class="a-offscreen"]')[0].get_attribute('textContent')
        except:
            original_price = current_price
        
        # Get Current Time
        last_updated = str(datetime.now())
        
        # Return details for update
        return [truncated_title, current_price, original_price, last_updated]
    
    # Start Webdriver
    driver = webdriver.Chrome()
    
    # Get Product Info
    product_info = [webscrapeAmazonUrl(url) for url in URL_LIST]
 
    # End Webdriver
    time.sleep(12)
    driver.quit()
    
    return product_info

print(webscrapeAmazon(["https://www.amazon.com/HENCKELS-Classic-Razor-Sharp-Engineered-Informed/dp/B00004RFMT/",
                       "https://www.amazon.com/Adjustable-Organizer-Fastening-Microfiber-Management/dp/B0B5DT4ZJ6/",
                       "https://www.amazon.com/Fastening-Adjustable-Organization-Microfiber-Management/dp/B08KH6WTJZ/",
                       "https://www.amazon.com/Duck-287908-Strength-Reusable-Outdoor/dp/B09ZMQHP1X/"]))
