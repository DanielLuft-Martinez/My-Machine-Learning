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

KERAS_REST_API_URL = "http://localhost:5000/test"

# load the input image and construct the payload for the request
dataset = [arg for arg in sys.argv[1:-1]]
epochs = sys.argv[-1]
payload = {
	"dataset": dataset,
	"epochs" : epochs,
	}
print("Verifying connection to server...")
try:
	print("Connected!")
	r = requests.get(KERAS_REST_API_URL)
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
