# USAGE
# python simple_request.py

# import the necessary packages
import requests
import sys
import base64
import os
from PIL import Image
import io

# initialize the Keras REST API endpoint URL along with the input
# image path

KERAS_REST_API_URL = "http://localhost:5001/predict"

# load the input image and construct the payload for the request

image_bytes = open("/home/matthew/Documents/school/sw-engineering/my-machine-learning/flask_server/server-tests/corgi.JPEG", "rb").read()
image = str(base64.encodebytes(image_bytes).decode("utf-8"))

payload = {
	"image" : image
}
print("Testing connection...")

try:
    r = requests.get("http://localhost:5001/")
    print("Connected!")
except:
    print("Could not verify connection to server")
    exit(1)

# submit the request
try:
	r = requests.post(KERAS_REST_API_URL, json=payload).json()
except:
	print("A connection error occurred during training")

# ensure the request was sucessful
if r["success"]:
	# loop over the predictions and display them
	print("Done")

# otherwise, the request failed
else:
	print("Request failed")
