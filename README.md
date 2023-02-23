Problem Statement: The goal of this project is to create an elastic image recognition web application service that can
automatically scale in and scale out horizontally based on traffic i.e. number of user requests and deploy
the application in a hybrid cloud environment created using AWS resources and Openstack (which runs
on our own hardware as a private cloud). Amazon Web Services, OpenStack provides the cloud resources
used. The web tier application which is deployed in the private cloud (i.e. OpenStack) takes images as
input from the user. This web tier application should connect to AWS resources like SQS Queue, S3 to
transmit the information to the App tier application which is deployed in public cloud (AWS) where the
image recognition model runs on the input image utilizing AWS resources for storage, transmission, and
computation of output. The Web tier application should also collect the final outputs from the App tier
and respond back to the initiated request. With a single server and limited resources, processing and
returning results to the user may take longer. Using AWS (Infrastructure as a Service)IaaS resources such
as EC2 instances, Simple Queue Services, and S3 storage, the app tier application should auto scale
horizontally based on number of requests/hits, handle multiple concurrent requests, and return responses
with low latency without missing out any requests. The desired characteristics of the application are Low
Latency, High Reliability, Auto Scaling

Architecture: As illustrated in the figure below, we have a virtual instance in the Web-Tier of the application
deployed in private cloud (i.e. OpenStack) that can receive images as input from multiple users
concurrently. This web tier application connects to the public cloud (i.e. AWS) and sends the image name
to the SQS Request Queue while also storing it as a key-object pair in the S3 Input Bucket for persistence.
The Web-Tier handles the algorithm that scales out the instances in the App-Tier based on the number of
incoming requests in the SQS Request Queue. The App-Tier instances which are deployed on-demand in
the public cloud (i.e. AWS) are in charge of reading images from SQS Request Queue and S3 Input
Bucket, processing them with the provided deep learning algorithm, and storing the output result in SQS
Response Queue and S3 Output Bucket. After processing the image, the App-Tier instance deletes the image from the SQS Request Queue and checks the SQS Request Queue for any pending requests to
process; if no image is found, the instance is terminated, resulting in scale-in; otherwise, the process is
repeated. If any of the App-Tier instances were unable to process the image for any reason, the image
name will be automatically visible to other instances due to the AWS SQS visibility timeout feature thus
ensuring Reliability to the system.

![image](https://user-images.githubusercontent.com/27536166/220840366-f85c343e-7a75-479b-8471-8cf972f1ed7e.png)
