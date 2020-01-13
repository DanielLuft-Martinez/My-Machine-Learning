'''
Created on Jul 18, 2019

@author: Daniel
'''

from tensorflow.python.keras.preprocessing.image import ImageDataGenerator
from keras_preprocessing.image import ImageDataGenerator


class DatGen(object):
    '''
    empty objects just used to carry methods
    '''


    def __init__(self):
        '''
        '''

        
    ## generates our training data, based on the flow from directory
    
    def generateTrainingData(self, img_proc_size, full_train_dir):
        
        datagen=ImageDataGenerator(rescale=1./255.)
        train_generator=datagen.flow_from_directory(full_train_dir,
                                                   target_size = (img_proc_size, img_proc_size),  # All images will be resized to img_proc_size x img_proc_size
                                                   batch_size = 10,
                                                   class_mode = 'categorical') 

        return train_generator
    
    ## generates our validation data, based on the flow from director
    
    def generateValidationData(self,  img_proc_size, full_valid_dir):
    

        test_datagen=ImageDataGenerator(rescale=1./255.)
        valid_generator=test_datagen.flow_from_directory(full_valid_dir,
                                                   target_size = (img_proc_size, img_proc_size),  # All images will be resized to img_proc_size x img_proc_size
                                                   batch_size = 10,
                                                   shuffle=False,
                                                   class_mode = 'categorical')
        return valid_generator
    
    ## generates our test data, based on the flow from director
    
    def generateTestData(self, img_proc_size, full_test_dir):
        

        test_datagen=ImageDataGenerator(rescale=1./255.)
        test_generator=test_datagen.flow_from_directory(full_test_dir,
                                                   target_size = (img_proc_size, img_proc_size),  # All images will be resized to img_proc_size x img_proc_size
                                                   batch_size = 1,
                                                   shuffle=False,
                                                   class_mode = 'categorical')
        return test_generator
    
    
    
    