from customModel import CustomMML

#my_model = CustomModel("test_model", "test_user")
#model = my_model.train("/home/matthew/Documents/school/sw-engineering/my-machine-learning/ML/initial_datasets", 1, 100, 2, 2)
#print("Test")
#model.predict()

cust_model = CustomMML("test_model", "/home/matthew/Documents/school/sw-engineering/my-machine-learning/ML/var_model_var_input/data/", "/home/matthew/Documents/school/sw-engineering/my-machine-learning/ML/var_model_var_input/output/" ,"test_user")
model = cust_model.train(2, 100, 1, 2)
a_model = cust_model.get_model()
print("done")