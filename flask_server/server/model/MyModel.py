'''
Created on Jul 18, 2019

@author: Daniel
'''


from tensorflow.python.keras import layers
from tensorflow.python.keras import Model


class MyModel(object):
    '''
    empty objects just used to carry methods
    '''


    def __init__(self):
        '''
        '''
    
    
    # Method ONLY makes model based on specifcations
    # make sure all vars are in agreement when in actual use
    # with other pieces
    
    def makeMyModel(self, num_conv, num_dd, num_inp, img_proc_size):


        img_input = layers.Input(shape = (img_proc_size, img_proc_size, 3))
        
        # First convolution extracts 16 filters that are 3x3
        # Convolution is followed by max-pooling layer with a 2x2 window
        x = layers.Conv2D(16, 3, activation = 'relu')(img_input)
        x = layers.MaxPooling2D(2)(x)
        if num_conv >= 2:
            # Second convolution extracts 32 filters that are 3x3
            # Convolution is followed by max-pooling layer with a 2x2 window
            x = layers.Conv2D(32, 3, activation = 'relu')(x)
            x = layers.MaxPooling2D(2)(x) 
        if num_conv >= 3:
            # thrid convolution extracts 64 filters that are 3x3
            # Convolution is followed by max-pooling layer with a 2x2 window
            x = layers.Conv2D(64, 3, activation = 'relu')(x)
            x = layers.MaxPooling2D(2)(x)
         
         
        # Flatten feature map to a 1-dim tensor
        x = layers.Flatten()(x)
        
        i = 1
        while i <=  num_dd:
            # Create a fully connected layer with ReLU activation and 512 hidden units
            x = layers.Dense(512, activation = 'relu')(x)
            
            # Add a dropout rate of 0.25
            x = layers.Dropout(0.25)(x)
            
            i+=1
          
    
        # Create output layer with a single node and sigmoid activation
        output = layers.Dense(num_inp, activation = 'softmax')(x)
    
        # Configure and compile the model
    
        model = Model(img_input, output)
        
        return model