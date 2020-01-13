"""
Created on Jul 15, 2019
@author Matthew Wark

Simple REST API for use in training and accessing custom machine learning models
created through the My Machine Learning App. The server supports training models
via json files passed via POST messages through the /create route, testing server
connectivity by a GET request to /, and predicting on a model through the /predict
branch. See code below, or accompanying documentation for json formatting for each
request type.

"""
import base64
import io
import os

import cv2
import flask
import numpy as np
import tensorflow as tf
from PIL import Image
from tensorflow.python.client import device_lib
from tensorflow.python.keras.initializers import glorot_uniform
from tensorflow.python.keras.models import load_model
from tensorflow.python.keras.preprocessing.image import img_to_array
from tensorflow.python.keras.utils import CustomObjectScope

from model.customModel import CustomModel

# initialize our Flask application and a global custom model object
app = flask.Flask(__name__)
my_model = None

# Prepares an image to be run through a model
# Rescales the image, resizes it and normalizes pixel values to values from 0 to 1
def prepare_image(image, target):
	image = image.resize(target)
	image = img_to_array(image)
	image = np.expand_dims(image, axis=0)
	norm_image = cv2.normalize(image, None, alpha=0, beta=1, norm_type=cv2.NORM_MINMAX, dtype=cv2.CV_32F)
	return norm_image


# Takes a b64 encoded image as a string and returns the image bytes
def get_json_image(img_str):
	image = None
	try:
		image = bytearray(base64.b64decode(img_str))
		print("Got image from json request")
	except:
		print("Error getting image")
	
	return image


# Generates and saves the plots generated while training the model
# Saves to user's model file
def get_trainining_results():
	output_files = os.listdir(my_model.save_dir)
	output_images = []
	for file in output_files:
		if ".png" in file:
			image_bytes = open(my_model.save_dir + file, "rb").read()
			output_images.append(base64.encodebytes(image_bytes).decode("utf-8"))
	return output_images


# Simple function to test connectivity to the server
@app.route("/", methods=["GET", "POST"])
def test_server():
	return "Server connected"

# Not tested yet
# This gives the option to load a previously saved model into the server for predictions
@app.route("/load", methods=["POST"])
def load_saved_model():
	data = {"success" : False}
	try:
		model_name = flask.request.json["model"]
		user_id = flask.request.json["user"]
	except:
		print("Error reading user information from json")
		return data
	
	# Check that the keras model and the saved custom model exist
	# May want to change this to pickle instead of a json file
	model_path = "../user_models/" + str(model_name) + "_" + str(user_id) + "/" + model_name + ".h5"
	if not os.path.exists(model_path):
		print("Error, keras model not found")
		return data
	
	# This is loading the saved custom model object
	custom_model_path = "../user_models/" + str(model_name) + "_" + str(user_id) + "/" + model_name + ".json"
	if not os.path.exists(custom_model_path):
		print("Error, custom model not found")
		return data
	
	# Load the custom model into the global model variable from the save file
	try:
		with open(custom_model_path, "r") as input_file:
			json_file = json.load(input_file)
			name = json_file["model_name"]
			user_id = json_file["user"]
		global my_model
		my_model = CustomModel(name, user_id)
		data["success"] = True
	except:
		print("Error, failed to load custom model")
		return data

	return data


# This is where the training for the app takes place
# Supports POST requests containing json that should have all of the 
@app.route("/create", methods=["POST"])
def create():
	# initialize the data dictionary that will be returned from the
	# view
	data = {"success": False}
	if flask.request.method == "POST":  
		# Get custom model data from POST
		model_name = flask.request.json["model"]
		user_id = flask.request.json["user"]
		epochs = int(flask.request.json["epochs"])
		proc_size = int(flask.request.json["proc_size"])
		conv_sets = int(flask.request.json["conv_sets"])
		dd_sets = int(flask.request.json["dd_sets"])
		dataset = flask.request.json["datasets"]

		# Setup model object and train with user specified data
		print("Creating new model from request...")
		custom_model = CustomModel(model_name, user_id)
		custom_model.train(epochs, dataset, proc_size,
                conv_sets, dd_sets)
		custom_model.save_model()

		# Set the global thread model = our custom model object
		global my_model
		my_model = custom_model
		data["images"] = get_trainining_results()

		# Indicate a successful request
		data["success"] = True

	# Return the results as a json file
	return flask.jsonify(data)


@app.route("/predict", methods=["POST"])
def predict():
	# initialize the data dictionary that will be returned from the
	# view
	data = {"success": False}
	# ensure an image was properly uploaded to our endpoint
	if flask.request.method == "POST":# and my_model is not None:
		image_bytes = get_json_image(flask.request.json["image"])
		image = Image.open(io.BytesIO(image_bytes))
		image = prepare_image(image, target=(my_model.proc_size, my_model.proc_size))

		# Load the keras model from the golbal custom model object's saved path
		keras_model = load_model(my_model.model_path)

		# Make the prediction using the keras model
		preds = keras_model.predict(image)[0]

		# Generate a 'heat map' image to give insight into prediction
		heat_map_path = my_model.visualize_layer_image(image)
		heat_map_bytes = open(heat_map_path, "rb").read()
		heat_map_image = base64.encodebytes(heat_map_bytes).decode("utf-8")
		data["image"] = heat_map_image
		
		# Loop over predictions and create a list of dictionaries containing
		# the formatted predictioins. Add to data dict
		data["predictions"] = []
		for index, label in enumerate(my_model.labels):
			r = {"label": label, "probability": (str(round(float(preds[index] * 100), 2)) + " %")}
			data["predictions"].append(r)
		print("Predictions:", preds)

		# Indicates successful response
		data["success"] = True

	# Return the results as a json file
	return flask.jsonify(data)

# Method for testing image decoding for predictions on the server
# Takes image from post message, decodes it and attempts to write to disk
@app.route("/test", methods=["POST"])
def test_predict():
	print("Testing image receive")
	data = {"success" : False}
	image = get_json_image(flask.request.json["image"])
	try:
		if image is not None:
			data["success"] = True
			with open("my-test.png", "wb+") as file:
				file.write(image)
	except:
		print("Failed writing image")

	return flask.jsonify(data)

# Load the server
# To run on a local machine only, remove the 'host' argument
# Running with 0.0.0.0 as the 'host' will expose the server to the LAN at port 5000
# by default
if __name__ == "__main__":
	print(("* Starting server..."
		"Please wait until server has fully started to make requests"))
	app.run(debug=False, host="0.0.0.0", port=5000)
