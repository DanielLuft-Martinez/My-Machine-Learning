'''
Created on Jul 18, 2019

@author: Daniel
'''


import os
import sys
import shutil
import tarfile
import json
from shutil import copyfile

import matplotlib.pyplot as plt
import matplotlib.image as mpimg

from tensorflow.python.keras import layers
from tensorflow.python.keras import Model
import tensorflow as tf

from tensorflow.python.keras.optimizers import RMSprop

from tensorflow.python.keras.preprocessing.image import ImageDataGenerator

import numpy as np

from tensorflow.python.keras.preprocessing.image import array_to_img, img_to_array, load_img
from tensorflow.losses import softmax_cross_entropy


from tensorflow.python.keras.layers import Dense, Activation, Flatten, Dropout, BatchNormalization
from tensorflow.python.keras.models import Sequential
from tensorflow.python.keras.layers import Conv2D, MaxPooling2D
from tensorflow.python.keras import regularizers, optimizers
from tensorflow.python.keras.models import load_model

import random
import PIL
from PIL import Image
import pandas as pd

import datetime

from model.FileFlow import FileFlow
from model.MyModel import MyModel
from model.DatGen import DatGen
from model.TestingProcess import TestingProcess
from model.TrainingProcess import TrainingProcess


class CustomModel(object):
    '''
    '''
    model = []

    def __init__(self, model_name, user_id):
        self.name = str(model_name)
        self.id = str(user_id)
        self.proc_size = -1
        self.labels = []
        try:
            cwd = os.getcwd() + "/"
            self.train_dir = cwd + "sample_data"
            self.save_dir = cwd + "user_models/" + self.name + "_" + self.id + "/"
            self.model_path = self.save_dir + self.name + ".h5"
            self.image_dir = self.save_dir + "images/"
            if not os.path.exists(save_dir):
                os.mkdir(save_dir)
            if not os.path.exists(save_dir):
                os.mkdir(image_dir)
        except:
            print("Directories exist")


    def train(self, epochs, dataset, proc_size,
                 num_conv_sets, num_dd_sets):
        '''
        
        Runs the whole shebang on init
        '''
        

        ## Arguments that effect model and training
        '''
        EPOCHS = 3   ## number of training runs [10 - 15 -20]
        IMAGE_PROC_SIZE = 100 ##   [50 - 100 - 150]
        
        num_conv_sets = 3 ## [1 - 2 - 3]
        num_dd_sets = 1 ## [1 - 2 - 3]
        
        iD = 'Manual_Testing'
        '''
        self.proc_size = proc_size
        PRED_THRESHOLD = .5 ## necessary confidence threshold to predict a label
        
        
        ## setting up the file structure
        
        flo = FileFlow()
        
        train_dir = flo._validateBaseDir(self.train_dir)

        labels = flo.getTarLabels(train_dir)
        labels = [label for label in labels if label in dataset]
        num_inp = len(labels)
        self.labels = labels
        
        flo.extractTarData(train_dir, labels)
        
        flo.createFileStructure(train_dir, labels)
        
        flo.seperateDatasets(train_dir, labels)
        
        
        ## creating output dir for results
        
        save_to = self.save_dir
        try:
            os.mkdir(save_to)
        except:
            pass
        
        ## Preparing data
        
        dg = DatGen()
        
        training_dir = train_dir + 'all/train' 
        valid_dir = train_dir + 'all/valid'
        test_dir = train_dir + 'all/test'
        
        train_gen = dg.generateTrainingData(self.proc_size,training_dir)
        valid_gen = dg.generateValidationData(self.proc_size, valid_dir)
        test_gen = dg.generateTestData(self.proc_size, test_dir)
        
        
        ## Constructing Model
        
        mml = MyModel()
        
        model = mml.makeMyModel(num_conv_sets, num_dd_sets, num_inp, self.proc_size)
        self.model = [model]
        
        ## Training
        
        trpr = TrainingProcess()
        
        history = trpr.trainModel(model, epochs, train_gen, valid_gen) 
        ## model is now trained
        
        trpr.generateVisualTrainingResults(history, save_to, self.id)
        
        ## Testing
        
        tepr = TestingProcess()
        
        pred = tepr.runTest(test_gen, model)
        
        tepr.generateTestingResults(labels, test_gen, pred, PRED_THRESHOLD, save_to, self.id)
        
        
        
        
        ## cleaning up
        
        flo.cleanUp(train_dir, labels)

    def get_model(self):
        return self.model[0]

    def save_model(self):
        if self.model is not None:
            save_to = self.save_dir + self.name + ".h5"
            self.model[0].save(save_to)
            with open(self.save_dir + self.name + ".json", "w+") as model_file:
                json.dump(self.model_to_dict(), model_file)

    
    def model_to_dict(self):
        model_dict = {}
        model_dict["save_dir"] = self.save_dir
        model_dict["name"] = self.name
        model_dict["model_path"] = self.model_path
        model_dict["train_dir"] = self.train_dir
        model_dict["id"] = self.id
        model_dict["labels"] = self.labels
        return model_dict

    def visualize_layer_image(self, norm_image):
        cur_model = load_model(self.model_path)
        if self.save_dir[-1] != '/':
            self.save_dir += '/'
        save_to = self.save_dir + "predictions/"
        tmp_dir = save_to + 'tmp/'
        try:
            os.mkdir(save_to)
        except:
            pass
        try:
            os.mkdir(tmp_dir)
        except:
            pass

        images = []
        img_input = cur_model.input
        successive_outputs = [layer.output for layer in cur_model.layers[1:]]
        visualization_model = Model(img_input, successive_outputs)
        np.seterr(divide='ignore', invalid='ignore')
        x = norm_image
        # Let's run our image through our network, thus obtaining all
        # intermediate representations for this image.
        successive_feature_maps = visualization_model.predict(x)

        # These are the names of the layers, so can have them as part of our plot
        layer_names = [layer.name for layer in cur_model.layers]

        # Now let's display our representations
        d = 0
        for layer_name, feature_map in zip(layer_names, successive_feature_maps):
            if len(feature_map.shape) == 4:
            # Just do this for the conv / maxpool layers, not the fully-connected layers
                n_features = feature_map.shape[-1]  # number of features in feature map
            # The feature map has shape (1, size, size, n_features)
                size = feature_map.shape[1]
            # We will tile our images in this matrix
                display_grid = np.zeros((size, size * n_features))
                for i in range(n_features):
                    x = feature_map[0, :, :, i]
                    x -= x.mean()
                    x /= x.std()
                    x *= 64
                    x += 128
                    x = np.clip(x, 0, 255).astype('uint8')
                # We'll tile each filter into this big horizontal grid
                    display_grid[:, i * size : (i + 1) * size] = x
                # Display the grid
                scale = 20. / n_features
                plt.figure(figsize=(scale * n_features, scale))
                plt.title(layer_name)
                plt.grid(False)
                plt.imshow(display_grid, aspect='auto', cmap='viridis')
                image_name = tmp_dir+'Layer_visualization_'+str(d)+'.png'
                plt.savefig(image_name, papertype=None, format=None,
                transparent=False, bbox_inches='tight',
                frameon=None, metadata=None)
                images.append(image_name)
                d+=1
            

            imgs    = [ Image.open(fi) for fi in images ]
            widths, heights = zip(*(fi.size for fi in imgs))
            max_width = max(widths)
            total_height = sum(heights)

            new_im = Image.new('RGB', (max_width, total_height))

            y_offset = 0
            for im in imgs:

                new_im.paste(im, (0, y_offset))
                y_offset += im.size[1]

            new_im.save(save_to+'user_image_layer_visualization.png')
        
        shutil.rmtree(tmp_dir)
        return save_to+'user_image_layer_visualization.png'