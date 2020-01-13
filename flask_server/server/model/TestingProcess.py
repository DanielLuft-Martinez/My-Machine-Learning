'''
Created on Jul 18, 2019

@author: Daniel
'''


import os
import sys
import tarfile
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
import matplotlib.image as mpimg
from sklearn import svm, datasets
from sklearn.metrics import confusion_matrix

from tensorflow.python.keras.optimizers import RMSprop


class TestingProcess(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
    
    
    
    ## testing
    
    def runTest(self, test_generator, model):
        
        test_generator.reset()
        
        testing_fnames = test_generator.filenames
        
        pred = model.predict_generator(test_generator,
            len(testing_fnames),
            verbose=2)
        
        return pred
    
    
    
    
    ## This is the basic confusion matrix code  example
    ## given by sklearn
    
    def plot_confusion_matrix(self, y_true, y_pred, classes,
                              normalize=False,
                              title=None):
        """
        This function prints and plots the confusion matrix.
        Normalization can be applied by setting `normalize=True`.
        """
        if not title:
            if normalize:
                title = 'Normalized confusion matrix'
            else:
                title = 'Confusion matrix, without normalization'
        
        params = {"ytick.color" : "w",
          "xtick.color" : "w",
          "axes.labelcolor" : "w",
          "axes.edgecolor" : "w",
          "axes.facecolor" : "k",
          "savefig.bbox" : "tight",
          "savefig.facecolor" : "k",
          "figure.autolayout" : True,
          "text.color" : "w"}
        plt.rcParams.update(params)
    
        # Compute confusion matrix
        cm = confusion_matrix(y_true, y_pred)
        # Only use the labels that appear in the data
        
        if normalize:
            cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
            
    
        fig, ax = plt.subplots() # ignore "error", just how pydev checks stuff
        im = ax.imshow(cm, interpolation='nearest', cmap=plt.cm.summer)
        ax.figure.colorbar(im, ax=ax)
        # We want to show all ticks...
        ax.set(xticks=np.arange(cm.shape[1]),
               yticks=np.arange(cm.shape[0]),
               # ... and label them with the respective list entries
               xticklabels=classes, yticklabels=classes,
               title=title,
               ylabel='True label',
               xlabel='Predicted label')
    
        # Rotate the tick labels and set their alignment.
        plt.setp(ax.get_xticklabels(), rotation=45, ha="right",
                 rotation_mode="anchor")
    
        # Loop over data dimensions and create text annotations.
        fmt = '.2f' if normalize else 'd'
        thresh = cm.max() / 2.
        for i in range(cm.shape[0]):
            for j in range(cm.shape[1]):
                ax.text(j, i, format(cm[i, j], fmt),
                        ha="center", va="center",
                        color="white" if cm[i, j] < thresh else "black")
        fig.tight_layout()
        return ax
    
    
    
    # generates the visual training results
    # some explanation--
    # needs the training history of a model : history
    # a place to save the files to : save_to : should be a dir that exists
    # test gen data from the set aside data.... 
    # and some sort of unique iD for the files : iD
    def generateTestingResults(self, labels, test_generator,
                               pred, pred_thresh, save_to, iD):
        
        if save_to[-1] != '/':
            save_to += '/'
        
        pred_bool = (pred > pred_thresh) 
        predictions = pred_bool.astype(int)
        

        rough_names = test_generator.filenames
        cleaned_names = []
        for name in rough_names:
            cleaned_names.append(name[name.find("\\")+1:])
            
        
        truth = []
        for name in cleaned_names:
            true_val = []
            for label in labels:
                true_val.append(1 if label in name else 0)
            truth.append(true_val)
            
        true_results = pd.DataFrame(truth, columns=labels)
        test_results=pd.DataFrame(predictions, columns=labels)
        
        
        ordered_cols=["filename"]+labels
        
        true_results["filename"]= cleaned_names
        true_results = true_results[ordered_cols]
        
        test_results["filename"]= cleaned_names
        test_results=test_results[ordered_cols]
        
        
        test_results.to_csv(save_to + 'results_test'+iD+'.csv',index=False)
        true_results.to_csv(save_to + 'results_true'+iD+'.csv',index=False)
        
        ## confusion_matrix stuff
        hold_tr = true_results.drop('filename', axis=1)
        
        #???cm = confusion_matrix(hold_tr.values.argmax(axis=1), predictions.argmax(axis=1))
        np.set_printoptions(precision=2)
        
        params = {"ytick.color" : "w",
          "xtick.color" : "w",
          "axes.labelcolor" : "w",
          "axes.edgecolor" : "w",
          "axes.facecolor" : "k",
          "savefig.facecolor" : "k",
          "savefig.bbox" : "tight",
          "figure.autolayout" : True,
          "text.color" : "w"}
        plt.rcParams.update(params)
        plt.figure()
        self.plot_confusion_matrix(hold_tr.values.argmax(axis=1), predictions.argmax(axis=1), labels)
        plt.tight_layout()
        plt.autoscale()
        plt.savefig(save_to + 'confusion_matrix_unnormalized'+iD+'.png')
        plt.clf()
        
        
        plt.figure()
        self.plot_confusion_matrix(hold_tr.values.argmax(axis=1), predictions.argmax(axis=1), labels, normalize=True)
        plt.tight_layout()
        plt.autoscale()
        plt.savefig(save_to + 'confusion_matrix_normalized'+iD+'.png')
        plt.clf()
        plt.close('all')


        

        
        
        
        
        