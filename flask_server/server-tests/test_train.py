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

KERAS_REST_API_URL = "http://localhost:5000/create"

# load the input image and construct the payload for the request

payload = {
	"model" : "my_test_model",
	"user" : "test_user_id",
	"datasets" : ["apple", "corgi", "jet"],
	"epochs" : 10,
	"proc_size" : 100,
	"conv_sets" : 3,
	"dd_sets" : 3
	}
print("Verifying connection to server...")
try:
	print("Connected!")
	r = requests.get(KERAS_REST_API_URL)
except:
	print("Could not verify connection to server")
	exit(1)
print("Training model...")

# submit the request
try:
	r = requests.post(KERAS_REST_API_URL, json=payload).json()
except:
	print("A connection error occurred during training")

# ensure the request was sucessful
if r["success"]:
	# loop over the predictions and display them
	print("Model trained and loaded in server")
	print("Saving results...")
	for idx, image in enumerate(r["images"]):
		image_bytes = bytearray(base64.b64decode(image))
		savepath = "training-results/image" + str(idx) + ".png"
		if os.path.exists("training-results/" + savepath):
			os.remove(savepath)
		with open(savepath, "wb+") as image:
			image.write(image_bytes)
			print("Saved image")
	print("Done")

# otherwise, the request failed
else:
	print("Request failed")
