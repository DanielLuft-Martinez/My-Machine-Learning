#! /bin/bash
sudo systemctl daemon-reload
sudo systemctl restart docker
sudo docker run --rm --runtime=nvidia -w /home/flask/server -p 5000:5000 --name keras-flask-server -it mtwark/mml-server python3 run_keras_server.py
