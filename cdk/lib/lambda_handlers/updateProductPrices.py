from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from datetime import datetime, timedelta
import boto3
import json
import time

UPDATE_PRODUCT_PRICE_EVERY = 24*7 # Hours

def updateProductPrices(event, context):
    try:
        # Create DynamoDB Client
        client = boto3.client("dynamodb")
        # Get All Table Entries
        data = client.scan(
            TableName = "productTrackingTable",
            ProjectionExpression = "productLink,historicLow,lastUpdated" 
        )
        if "Items" in data:
            # Get Product Links That Need To Be Updated
            data_to_update = []
            for item in data["Items"]:
                if "lastUpdated" in item:
                    if datetime.now()-timedelta(hours = UPDATE_PRODUCT_PRICE_EVERY) > datetime.strptime(item["lastUpdated"]['S'],"%Y-%m-%d %H:%M"):
                        data_to_update.append((item["productLink"]['S'],item["historicLow"]['S'],item["lastUpdated"]['S']))
                else:
                    data_to_update.append((item["productLink"]['S'], None, None))
            # Webscrape Amazon For Each Link
            webscrape_results = webscrapeAmazon([item[0] for item in data_to_update])
            updated_data = []
            for i in range(len(data_to_update)):
                if webscrape_results[i] == None:
                    continue
                productLink = data_to_update[i][0]
                productName = webscrape_results[i][0]
                originalPrice = webscrape_results[i][2]
                currentPrice = webscrape_results[i][1]
                if data_to_update[i][1] != None:
                    historicLow = "$"+str(round(min(float(data_to_update[i][1][1:]),float(webscrape_results[i][1][1:])),2))
                else:
                    historicLow = currentPrice
                lastUpdated = webscrape_results[i][3]
                updated_data.append((productLink, productName, originalPrice, currentPrice, historicLow, lastUpdated))
            # Update productTrackingTable
            for i in range(0,len(updated_data),25):
                updated_data_seg = updated_data[i:min(i+25,len(updated_data))]
                client.batch_write_item(
                    RequestItems = {
                        "productTrackingTable" : [
                            { "PutRequest": { "Item": { 
                                "productLink": { 'S':item[0] },
                                "productName": { 'S':item[1] },
                                "originalPrice": { 'S':item[2] },
                                "currentPrice": { 'S':item[3] },
                                "historicLow": { 'S':item[4] },
                                "lastUpdated": { 'S':item[5] },  
                            } } }
                            for item in updated_data_seg
                        ]
                    }
                )
        # Return Response
        print("Successfully updated productTrackingTable at " + str(datetime.now()))
        return {
            "statusCode": "200",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "Updated productTrackingTable at " + str(datetime.now())
            })
        }
    except:
        print("Failed to update productTrackingTable DynamoDB")
        return {
            "statusCode": "503",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "Failed to update productTrackingTable DynamoDB For An Unknown Reason"
            })
        }

def webscrapeAmazon(URL_LIST):
    def webscrapeAmazonUrl(URL, retry=2):
        # If out of retries, return None
        if retry == 0:
            return None
        # Navigate to Amazon
        driver.get(URL)
        time.sleep(.5)
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
            try:
                current_price = driver.find_elements(By.XPATH, '//span[@class="a-price a-text-price a-size-medium apexPriceToPay"]//span[@class="a-offscreen"]')[0].get_attribute('textContent')
            except:
                return webscrapeAmazonUrl(URL, retry-1)
        # Get Product Original Price
        try:
            original_price = driver.find_elements(By.XPATH, '//span[@data-a-strike="true" and @class="a-price a-text-price" and @data-a-color="secondary"]/span[@class="a-offscreen"]')[0].get_attribute('textContent')
        except:
            original_price = current_price
        # Get Current Time
        last_updated = str(datetime.now().strftime("%Y-%m-%d %H:%M"))
        # Return details for update
        return [truncated_title, current_price, original_price, last_updated]
    # Start Webdriver
    options = Options()
    options.binary_location = '/opt/headless-chromium'
    options.add_argument('--headless')
    options.add_argument('--no-sandbox')
    options.add_argument('--single-process')
    options.add_argument('--disable-dev-shm-usage')
    driver = webdriver.Chrome('/opt/chromedriver',options=options)
    # Get Product Info
    product_info = [webscrapeAmazonUrl(url) for url in URL_LIST]
    # End Webdriver
    time.sleep(.5)
    driver.close()
    driver.quit()
    return product_info