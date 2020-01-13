'''
Created on Jul 18, 2019

@author: Daniel
'''

from tensorflow.python.keras.preprocessing.image import ImageDataGenerator
from tensorflow.python.keras import layers
from tensorflow.python.keras import Model
import shutil
from keras_preprocessing.image import ImageDataGenerator
from tensorflow.python.keras.layers import Dense, Activation, Flatten, Dropout, BatchNormalization
from tensorflow.python.keras.models import Sequential
from tensorflow.python.keras.layers import Conv2D, MaxPooling2D
from tensorflow.python.keras import regularizers, optimizers
import pandas as pd
import numpy as np
import datetime
import matplotlib.pyplot as plt

from tensorflow.python.keras.optimizers import RMSprop

class TrainingProcess(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
        
    ## does what it says, but all pieces should be in agreement
    
    def trainModel(self, model, epochs, train_generator, valid_generator):
        
        model.compile(loss = 'categorical_crossentropy',
                optimizer = RMSprop(lr = 0.001),
                metrics = ['acc'])
        
        history = model.fit_generator(generator=train_generator,
                    steps_per_epoch = 100, 
                    epochs = epochs,
                    validation_data = valid_generator,
                    validation_steps = 50,  
                    verbose = 1)
        
        return history # the history of the trained model
    
    # generates the visual training results
    # some explanation--
    # needs the training history of a model : history
    # a place to save the files to : save_to : this should be a dir that exists
    # and some sort of unique iD for the files : iD
    
    def generateVisualTrainingResults(self, history, save_to, iD):
        
        if save_to[-1] != '/':
            save_to += '/'
        
        
        
        acc = history.history['acc']
        val_acc = history.history['val_acc']
        
        # Retrieve a list of list results on training and test data
        # sets for each training epoch
        loss = history.history['loss']
        val_loss = history.history['val_loss']
        
        # Get number of epochs
        epochs = range(len(acc))
        params = {"ytick.color" : "w",
          "xtick.color" : "w",
          "axes.labelcolor" : "w",
          "axes.edgecolor" : "w",
          "axes.facecolor" : "k",
          "savefig.facecolor" : "k",
          "text.color" : "w"}
        plt.rcParams.update(params)
        
        # Plot training and validation accuracy per epoch
        plt.plot(epochs, acc)
        plt.plot(epochs, val_acc)
        plt.title('Training and validation accuracy')
        
        plt.savefig(save_to + 'plot_acc' + iD + '.png')
        plt.clf()
        
        # Plot training and validation loss per epoch
        plt.plot(epochs, loss)
        plt.plot(epochs, val_loss)
        plt.title('Training and validation loss')
        
        plt.savefig(save_to + 'plot_loss' + iD + '.png')
        plt.clf()
        plt.close()
        
        