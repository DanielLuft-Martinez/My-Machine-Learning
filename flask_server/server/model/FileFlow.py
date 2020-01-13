'''
Created on Jul 18, 2019

@author: Daniel
'''
import os
import tarfile
import shutil

class FileFlow(object):
    '''
    empty objects just used to carry methods
    '''


    def __init__(self):
        '''
        '''
    
    ## Ensures base dir is in proper format
    
    def _validateBaseDir(self, base_dir):
        if base_dir[-1] != '/':
            base_dir = base_dir + '/'
        return base_dir
    
    
    ## gets the tar files that have the data, the file names become Labels
    
    def getTarLabels(self, base_dir):
        
        base_dir = self._validateBaseDir(base_dir)
        all_files = os.listdir(base_dir)
        labels = []
        for fname in all_files:
            if '.tar'  in fname[-4:]:
                labels.append(fname[:-4])
        labels.sort()
        num_inp = len(labels)
        
        
        
        if num_inp > 5:
            print('Number of inputs: '+str(num_inp))
            print('Warning: Using more then 5 data sets will decrease accuracy and increase training time.')

        return labels

    
    ## Untarring into individual datasets
    ## this outputs as files and a file structure
    
    def extractTarData(self, base_dir, labels):
        
        base_dir = self._validateBaseDir(base_dir)
        try:
            for label in labels:    
                tar_file = label+'.tar'
                local_tar = base_dir + tar_file
                tar_ref = tarfile.TarFile(local_tar, 'r')
                if os.path.exists(base_dir + label):
                    shutil.rmtree(base_dir + label)
                tar_ref.extractall(base_dir+label)
                tar_ref.close()
        except:
            print("Error unzipping training data. May already be unzipped")
    
        print(label+' untarred')

    
    ## Creates file structure necessary for the use of flow_from_directory
    
    def createFileStructure(self, base_dir, labels):
        
        base_dir = self._validateBaseDir(base_dir)
       
        try:
            os.mkdir(base_dir+'all') 
            os.mkdir(base_dir+'all/train')
            os.mkdir(base_dir+'/all/valid')
            os.mkdir(base_dir+'/all/test')
        except:
            pass
        places = ['/train','/valid','/test']

        ## Creating directories for each of the inputs
        
        for place in places:
            for label in labels:
                try:
                    os.mkdir(base_dir+'all'+place+'/'+label)
                except:
                    pass
                
        ## doesn't output anything, but does modify file structure
        ## be sure to clean up with cleanFileStructure(), to keep env pristine
        
        
        


    ## Placing individual pictures into file structure, renaming them in the process
    ## currently the default train/valid/test split is 7/2/1
    ## using a counting remainder method 
        
    def seperateDatasets(self, base_dir, labels):
        
        base_dir = self._validateBaseDir(base_dir)
        
        for label in labels:
            i = 0 # counting and labeling files
            src = base_dir + label + '/'
            des = ''  
            
            ## could add randomizer to repick 7/8/9
            
            for file in os.listdir(src):
                if i % 10 == 7 or i % 10 == 8:
                    des = base_dir + 'all/valid/' +label
                elif i % 10 == 9:
                    des = base_dir + 'all/test/' + label
                else:
                    des = base_dir + 'all/train/' + label
                I =str(i)
                shutil.copyfile(src+'/'+file, des+'/'+label+"_"+I+'.jpg')
                i+=1


    ## removes any files created in service of results
    ## should be run after
    
    def cleanUp(self, base_dir, labels):
        
        base_dir = self._validateBaseDir(base_dir)
        
        for label in labels:        
            shutil.rmtree(base_dir+label)
        shutil.rmtree(base_dir+'all')
        
        
