Starting the Server

  From the Docker image:
	- Edit the second to last part of the start_server.sh script to the image id (or name) of the docker image
	- Run start_script.sh

  Directly:
	- Run python3 run_keras_server.py from the server directory

  All:
	- The server is set by default to be accessible by anybody on the local network with the host's ip address
	- The server is set to run on port 5000 by default
	- To access the server remotely, set your router to forward port 5000 to your host's ip address
	- If changing the port for the server and using the container, you must update the run script to reflect that
		- Ex: -p 80:5001 will forward traffic to the host on port 80 to the container on port 5001

Server functionality:

  Supported functions and routes:
	- Route: /
	- Method: GET
	- Result: String with text "Server connected"

	- Route: /create
	- Method: POST
	- Input: json file containing the following fields:
		- train : boolean
		- sample : boolean
		- dataset : [string]
		- epochs : int
		- proc_size : int
		- conv_sets : int
		- dd_sets : int
		- user_id : string
		- model : string
	- Response: json file containing the following fields
		- images: [base64 encoded training images]
		- success: boolean

	- Route: /predict
	- Method: POST
	- Input: json file containing the following fields:
		- user_id : string
		- model : string
		- image : base64 encoded images
	- Response: json file containing the following fields
		- image: base64 encoded heatmap
		- success: boolean