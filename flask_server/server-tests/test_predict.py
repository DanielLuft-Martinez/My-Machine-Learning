# USAGE
# python simple_request.py

# import the necessary packages
import requests
import sys
import base64

# initialize the Keras REST API endpoint URL along with the input
# image path

if len(sys.argv) < 2:
	print("Please specify a file to test.\nUsage:$ python simple_request.py <filepath>")
	exit(1)

KERAS_REST_API_URL = "http://localhost:5001/predict"
IMAGE_PATH = sys.argv[1]

# load the input image and construct the payload for the request
image = open(IMAGE_PATH, "rb").read()
payload = {"image": base64.b64encode(image).decode("utf-8")}

# submit the request
r = requests.post(KERAS_REST_API_URL, files=payload).json()

# ensure the request was sucessful
if r["success"]:
	# loop over the predictions and display them
	for (i, result) in enumerate(r["predictions"]):
		print("{}. {}: {:.4f}".format(i + 1, result["label"],
			result["probability"]))

# otherwise, the request failed
else:
	print("Request failed")
