from tensorflow.python.keras.models import load_model
from tensorflow.python.keras.applications import imagenet_utils
from tensorflow.python.keras.preprocessing.image import img_to_array
from tensorflow.python.keras.preprocessing.image import ImageDataGenerator
import numpy as np
from PIL import Image
import io
import cv2

def prepare_image(image, target):
	if image.mode != "RGB":
		image = image.convert("RGB")

	# resize the input image and preprocess it
	image = image.resize(target)
	image = img_to_array(image)
	image = np.expand_dims(image, axis=0)
	norm_image = cv2.normalize(image, None, alpha=0, beta=1, norm_type=cv2.NORM_MINMAX, dtype=cv2.CV_32F)
	#print(norm_image)
	return norm_image

base_dir = "/home/matthew/Documents/school/sw-engineering/my-machine-learning/flask_server/server-tests/"
images = [base_dir + label + ".JPEG" for label in ["n07739344_397", "n02112826_82", "n04552348_307"]]
for img in images:
	with open(img, "rb") as file:
		image = file.read()
	image = Image.open(io.BytesIO(image))
	image = prepare_image(image, target=(100, 100))
	model = load_model("/home/matthew/Documents/school/sw-engineering/my-machine-learning/flask_server/user_models/my_test_model_test_user_id/my_test_model.h5")
	preds = model.predict(image)[0]
	for idx, label in enumerate(["apple", "corgi", "jet"]):
		print(label + ": " + str(preds[idx]))
	print("\n")