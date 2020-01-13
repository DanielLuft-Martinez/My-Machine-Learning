Setting up the Docker container
	1. Follow instructions here to get docker setup with support for Nvidia gpus in
	   tensorflow: https://www.tensorflow.org/install/docker
		- Currently limited to linux machines with the proper nvidia runtime support installed

	2. From the 'flask server' directory containing the setup folder, run the build_docker.sh script
		- This will create a docker image with all of the needed tools and libraries to run the
		  server with Nvidia GPU support in keras/tensorflow.
		- This will also copy over the needed server files and sample images to the container image

	3. From the same directory, run: 
	   sudo docker run --rm --runtime=nvidia -p 5000 --name keras-flask-server -it <image id> bash
		- This causes the container to startup with traffic to the host's port 5000 to the container's
		  port 5000
		- A bash shell should start in the container
	4. Verify that the files all copied over successfully and that the server runs properly

	5. In a new terminal window on the host machine, run the command:
	   docker ps -a
	   	- This will display all running containers

	6. Copy the container id for keras-flask-server and run the command:
	   docker commit <container id> mml-server
	   	- This will save the current container to the image. Use this command any time you make changes
		  to the container that need to be retained.
		- Feel free to close the container at this point by typing exit in the shell

	7. To start up the docker container again from this point, run the start_server.sh script
