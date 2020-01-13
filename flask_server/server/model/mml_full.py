'''
Created on Jul 18, 2019

@author: Daniel
'''


import os
import sys
import shutil
import tarfile
from shutil import copyfile

import matplotlib.pyplot as plt
import matplotlib.image as mpimg

from tensorflow.python.keras import layers
from tensorflow.python.keras import Model
import tensorflow as tf

from tensorflow.python.keras.optimizers import RMSprop

from tensorflow.python.keras.preprocessing.image import ImageDataGenerator

import numpy as np
import random
from tensorflow.python.keras.preprocessing.image import img_to_array, load_img


from tensorflow.python.keras.preprocessing.image import array_to_img, img_to_array, load_img
from tensorflow.losses import softmax_cross_entropy


from tensorflow.python.keras.layers import Dense, Activation, Flatten, Dropout, BatchNormalization
from tensorflow.python.keras.models import Sequential
from tensorflow.python.keras.layers import Conv2D, MaxPooling2D
from tensorflow.python.keras import regularizers, optimizers

import datetime

from FileFlow import FileFlow
from MyModel import MyModel
from DatGen import DatGen
from TestingProcess import TestingProcess
from TrainingProcess import TrainingProcess



base_dir = str(sys.argv[1]) 


## Arguments that effect model and training

EPOCHS = 3   ## number of training runs [10 - 15 -20]
IMAGE_PROC_SIZE = 100 ##   [50 - 100 - 150]

num_conv_sets = 3 ## [1 - 2 - 3]
num_dd_sets = 1 ## [1 - 2 - 3]

iD = 'Manual_Testing'

PRED_THRESHOLD = .5 ## necessary confidence threshold to predict a label


## setting up the file structure

flo = FileFlow()

base_dir = flo._validateBaseDir(base_dir) 

labels = flo.getTarLabels(base_dir)
num_inp = len(labels)

flo.extractTarData(base_dir, labels)

flo.createFileStructure(base_dir, labels)

flo.seperateDatasets(base_dir, labels)


## creating output dir for results

save_to = base_dir+'output_'  + iD
os.mkdir(save_to)


## Preparing data

dg = DatGen()

train_dir = base_dir + 'all/train' 
valid_dir = base_dir + 'all/valid'
test_dir = base_dir + 'all/test'

train_gen = dg.generateTrainingData(IMAGE_PROC_SIZE,train_dir)
valid_gen = dg.generateValidationData(IMAGE_PROC_SIZE, valid_dir)
test_gen = dg.generateTestData(IMAGE_PROC_SIZE, test_dir)


## Constructing Model

mml = MyModel()

model = mml.makeMyModel(num_conv_sets, num_dd_sets, num_inp, IMAGE_PROC_SIZE)


## Training

trpr = TrainingProcess()

history = trpr.trainModel(model, EPOCHS, train_gen, valid_gen)

trpr.generateVisualTrainingResults(history, save_to, iD)

## Testing

tepr = TestingProcess()

pred = tepr.runTest(test_gen, model)

tepr.generateTestingResults(labels, test_gen, pred, PRED_THRESHOLD, save_to, iD)




## cleaning up

flo.cleanUp(base_dir, labels)

