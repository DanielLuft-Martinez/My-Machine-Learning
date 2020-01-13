FROM tensorflow/tensorflow:latest-gpu-py3

CMD ["mkdir", "-p", "/home/flask/server/model", "/home/flask/user_models", "/home/flask/sample_data"]

COPY server /home/flask/server
COPY user_models /home/flask/user_models
COPY sample_data /home/flask/sample_data

RUN apt update
RUN apt install nano
RUN apt install -y libsm6 libxext6 libxrender-dev
RUN pip3 install Pillow keras matplotlib sklearn flask requests pandas opencv-python