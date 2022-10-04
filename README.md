# General info
This project consists of 4 different microservices.
 - [multiplayer](multiplayer): exposing a REST API which tells a user that
   multiplayer has been enabled for him only if he is located in the US and he has used the game more than 5 times (based on the number of API calls).
 - [customer-support](customer-support) exposing a REST API which tells a user that customer-support
   has been enabled for him only on work days between 9:00 - 15:00 Slovenian time, because only then support personnel is available.
 - [ads](ads) exposing a REST API which tells a user that ads have been enabled for him only
   if a call to an external REST API confirms that's the case.
 - [fun-7-backend-core](fun-7-backend-core): exposes 2 REST APIs:
   - **Check services API**: Returning the status of the three previous microservices for the logged-in user (logged-in user must have user role **USER**).  
   - **Admin API**: used by administrators to enable managing of users (logged-in user must have user role **ADMIN**).

All 4 services are built using **Java 17**.  
[ads](ads) uses **maven-3.8.6** while all other microservices use **gradle-7.5** as a build/dependency management solution.  
They are all **Spring boot 2.7.3** REST Server applications.

A Swagger UI is available with mapping **/swagger-ui/index.html** for all 4 microservices.
The Swagger UI however does not expose the [fun-7-backend-core](fun-7-backend-core) microservice login and refresh_token endpoints described below.

[V1_\_init_db.sql](fun-7-backend-core/data-access/src/main/resources/db/migration/V1__init_db.sql) is a DB init script which gets executed by [FlyWay](https://flywaydb.org/documentation/usage/plugins/springboot)
the first time we run [fun-7-backend-core](fun-7-backend-core) and creates the DB schema and initial test data.

### fun-7-backend-core login endpoint
To log in to the [fun-7-backend-core](fun-7-backend-core) microservice and use its APIs we are using a basic login POST endpoint taking HTTP parameters **username** and **password** as input (sample request below).
``` http request
POST http://localhost:9093/login?username=myUsername&password=myPassword
```
[V1_\_init_db.sql](fun-7-backend-core/data-access/src/main/resources/db/migration/V1__init_db.sql) Contains the inserts for usernames and passwords
which can be used by the login endpoint. i.e. **username=user_US&password=password_US** (both taken from V1__init_db.sql)  
The login endpoint returns a JSON response containing JSON Web Token (JWT) type, access and refresh token.
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyZWZyZXNoRHVyYXRpb24iOjEyM30.-gyh--a5k3LFQS4U2Lyjv_YTGge_y9na0dsszY0nm8A"
}
```
> **⚠ Warning**  
> All other APIs in this microservice can ONLY be accessed with a valid **accessToken** returned by this API.  
> They require for it to be provided in the Authorization header of their request as seen below!
> ```
> Authorization:Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
> ```
> Whenever the **accessToken** expires a call to endpoint `GET http://<host>:<port>/access/refresh_token` is needed, to get a new valid access token.
> This call must have the login response **refreshToken** in the Authorization header as seen below:
> ```
> Authorization:Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyZWZyZXNoRHVyYXRpb24iOjEyM30.-gyh--a5k3LFQS4U2Lyjv_YTGge_y9na0dsszY0nm8A
> ```
> The **refresh_token** reply will be the same type of JSON message as after calling the login endpoint,
> it will also contain the same exact **refreshToken**, but it will return a new valid **accessToken** for other APIs to use.  
> **After the refreshToken also expires a new call to the login endpoint is needed to get a new accessToken/refreshToken set**

The duration of the **accessToken** and **refreshToken** is configurable through the 2 parameters below found in [application.properties](fun-7-backend-core/server/src/main/resources/application.properties)
``` properties
#**spring security and JWT configuration**
#refresh token/play session expiry in milliseconds. Default is 60 minutes: 60*60*1000=3600000
app.security.expiry.refresh-token=3600000
#access token expiry in milliseconds. Default is 10 minutes: 10*60*1000=600000
app.security.expiry.access-token=600000
```
The secret used to encode both JWTs is hardcoded in constant [JwtUtility.SECRET_KEY](fun-7-backend-core/controller/src/main/java/com/zlatkosh/security/JwtUtility.java)


## Unit and Integration testing
Unit testing in these modules is performed using Junit 5 (Jupiter).  
Integration testing on the Database layer is performed using:
- **A Postgres Testcontainer**: which gets started when running the unit tests and destroyed when done.
    > **Note:** This means the Docker daemon must be running on the machine executing these tests.
- [V1_\_init_db.sql](fun-7-backend-core/data-access/src/main/resources/db/migration/V1__init_db.sql)
is a DB init script which gets executed by [FlyWay](https://flywaydb.org/documentation/usage/plugins/springboot)
and creates the DB schema and initial test data.
- GWT unit tests inside Junit Jupiter @Nested classes  


## Setting the applications up in Google Cloud
![Architectural diagram](metadata/Architectural_diagram.png)
A Google Cloud account is needed to deploy this project on GCloud. Currently, GCloud offers a free 90 day trial with 300$ US credit. Please follow the steps below in Sequence to set the application up in Google Cloud.
 - From the Google Cloud web console create a new project named **fun-7-project** with Project ID: **fun-7-gcloud-project** (top left dropdown)
 - From the GCloud web console activate **Cloud Shell** (shell icon on the top right of the screen).
 - Steps needed to create the Postgres DB instance
   - From the Gcloud web console Enable **Service Networking API**
   - From **Cloud Shell** execute the following commands in a sequence to set up:
     ``` shell
     #Set your default project to fun-7-gcloud-project
     gcloud config set core/project fun-7-gcloud-project

     #Reserve an IP address range in your network
     gcloud beta compute addresses create fun7-private-services-access-range --global --prefix-length=24 --description="fun7 backend private services access range" --network=default --purpose=vpc_peering

     #Create a private connection between your network and the private services access network
     gcloud services vpc-peerings connect --service=servicenetworking.googleapis.com --ranges=fun7-private-services-access-range --network=default --project=fun-7-gcloud-project
     ```
   - Follow the [Create a new Postgres instance](https://cloud.google.com/sql/docs/postgres/create-instance#console) steps, creating the instance with the below configuration parameters in mind:
     - **Instance ID**: fun-7-backend-postgres
     - **Password**: The DB password you will use for DB username **postgres**
     - After selecting all the options the **Summary** section on the right side of your screen should look something like  
     ![img.png](metadata/PostgresInstanceSummary.png)
 - To access the newly created Google Cloud Postgres instance from our local machine we first need to follow the [Configuring Google Cloud SDK](https://www.jetbrains.com/help/datagrip/connect-to-google-cloud-sql-instances.html#configuring-google-cloud-sdk) steps (if we don't already have Google Cloud CLI set up) and after that the [Configuring connections with the Cloud SQL Proxy client](https://www.jetbrains.com/help/datagrip/connect-to-google-cloud-sql-instances.html#configuring-connections-with-the-cloud-sql-proxy-client) steps.  
Doing this will set up the CLI **Cloud SQL Proxy client**, which communicates to our database through a secure tunnel.  
If we followed all previous steps exactly the last CLI command we executed should have been
    ```bash
    #the below command maps the GCloud Postgres instance we created to localhost on port 3306
    cloud_sql_proxy.exe -instances=fun-7-gcloud-project:europe-west1:fun-7-backend-postgres=tcp:3306
    ```
   The above command would enable any DB client on our local machine to connect to our GCloud Postgres DB on host **127.0.0.1** (localhost) and port **3306**, same way as it would to any locally installed DB instance.
   > **Note:** the command should be executed after each restart!  
- **Create a Kubernetes Engine cluster:** execute the below script in a terminal:
  ```bash
  gcloud container --project "fun-7-gcloud-project" clusters create-auto "fun7-k8s-engine-cluster-1" --region "europe-west1" --release-channel "regular" --network "projects/fun-7-gcloud-project/global/networks/default" --subnetwork "projects/fun-7-gcloud-project/regions/europe-west1/subnetworks/default" --cluster-ipv4-cidr "/17" --services-ipv4-cidr "/22"
  ```
  > **Note:**  After creating the k8s cluster follow the [install gke-gcloud-auth-plugin](https://cloud.google.com/blog/products/containers-kubernetes/kubectl-auth-changes-in-gke) steps to be able to continue using **kubectl**.
- To deploy the 4 microservices to the newly created k8s cluster:
  - Execute the preparation steps below in a bash/shell:
    ```bash 
    #set the PROJECT_ID environment variable Windows (it's a bit different for Linux)
    set PROJECT_ID=fun-7-gcloud-project
    
    #Create the Google Cloud artifact registry where we'll push docker images for our project
    gcloud artifacts repositories create fun-7-gcloud-project-repo --repository-format=docker --location=europe-west1 --description="Docker repository fun-7-gcloud-project"
    
    #Configure the Docker command-line tool to authenticate to Artifact Registry:
    gcloud auth configure-docker europe-west1-docker.pkg.dev
    ```
  - **customer-support** microservice deployment steps:
    ```bash    
    #Build the docker image for this microservice. Run the command below from it's module directory (the one that contains **Dockerfile**)
    docker build -t europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/customer-support:latest .
    
    #Push the above built image to Artifact Registry
    docker push europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/customer-support:latest
    ```
    In the same Bash window set current directory to [k8s-engine/services/customer-support](k8s-engine/services/customer-support) and execute:
    ```bash  
    #Create a ConfigMap from application-kube.properties
    kubectl create configmap customer-support-configmap-generator --from-file=application-kube.properties

    #deploys the application to k8s 
    kubectl apply -f deployment.yaml
    
    #creates an endpoint for communication between k8s cluster members
    kubectl apply -f service.yaml
    
    #creates an endpoint for external inbound calls to the application
    kubectl apply -f ingres.yaml
    ```
    > **⚠ Warning**: the last command above is only needed to test the application
    from a local machine but should not be executed for a PROD environment!
    Only **fun-7-backend-core** should have inbound calls enabled in PROD!
  - **ads** microservice deployment steps:
    ```bash    
    #Build the docker image for this microservice. Run the command below from it's module directory (the one that contains **Dockerfile**)
    docker build -t europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/ads:latest .
    
    #Push the above built image to Artifact Registry
    docker push europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/ads:latest
     
    #Create a k8s Secret for the external ADS provider username and password
    #FIRST REPLACE <username> and <password> with the actual values!
    kubectl create secret generic ads-credentials --from-literal adPartnerUsername=<username> --from-literal adPartnerPassword=<password>
    ```
    In the same Bash window set current directory to [k8s-engine/services/ads](k8s-engine/services/ads) and execute:
    ```bash  
    #Create a ConfigMap from application-kube.properties
    kubectl create configmap ads-configmap-generator --from-file=application-kube.properties

    #deploys the application to k8s 
    kubectl apply -f deployment.yaml
    
    #creates an endpoint for communication between k8s cluster members
    kubectl apply -f service.yaml
    
    #creates an endpoint for external inbound calls to the application
    kubectl apply -f ingres.yaml
    ```
    > **⚠ Warning**: the last command above is only needed to test the application
    from a local machine but should not be executed for a PROD environment!
    Only **fun-7-backend-core** should have inbound calls enabled in PROD!
  - **multiplayer** microservice deployment steps:
    ```bash    
    #Build the docker image for this microservice. Run the command below from it's module directory (the one that contains **Dockerfile**)
    docker build -t europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/multiplayer:latest .
    
    #Push the above built image to Artifact Registry
    docker push europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/multiplayer:latest
    ```
    In the same Bash window set current directory to [k8s-engine/services/multiplayer](k8s-engine/services/multiplayer) and execute:
    ```bash
    #Create a ConfigMap from application-kube.properties
    kubectl create configmap multiplayer-configmap-generator --from-file=application-kube.properties

    #deploys the application to k8s 
    kubectl apply -f deployment.yaml
    
    #creates an endpoint for communication between k8s cluster members
    kubectl apply -f service.yaml
    
    #creates an endpoint for external inbound calls to the application
    kubectl apply -f ingres.yaml
    ```
    > **⚠ Warning**: the last command above is only needed to test the application
    from a local machine but should not be executed for a PROD environment!
    Only **fun-7-backend-core** should have inbound calls enabled in PROD!
  - **fun-7-backend-core** microservice deployment steps:
    ```bash    
    #Build the docker image for this microservice. Run the command below from it's module directory (the one that contains **Dockerfile**)
    docker build -t europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/fun-7-backend-core:latest .
    
    #Push the above built image to Artifact Registry
    docker push europe-west1-docker.pkg.dev/%PROJECT_ID%/fun-7-gcloud-project-repo/fun-7-backend-core:latest
     
    #Create a k8s Secret for the Postgres DB username and password
    #FIRST REPLACE <username> and <password> with the actual values!
    kubectl create secret generic fun-7-backend-core-credentials --from-literal dbUsername=<username> --from-literal dbPassword=<password>
    ```
    In the same Bash window set current directory to [k8s-engine/services/fun-7-backend-core](k8s-engine/services/fun-7-backend-core) and execute:
    ```bash  
    #Create a ConfigMap from application-kube.properties
    kubectl create configmap fun-7-backend-core-configmap-generator --from-file=application-kube.properties --from-file=DB_datasource.properties

    #deploys the application to k8s 
    kubectl apply -f deployment.yaml
    
    #creates an endpoint for communication between k8s cluster members
    kubectl apply -f service.yaml
    
    #creates an endpoint for external inbound calls to the application
    kubectl apply -f ingres.yaml
    ```
## External calls to the Google Cloud deployed microservices
For development purposes all 4 microservices have an ingres component created exposing their endpoints remotely.  
As already mentioned in the previous section for a PROD environment only [fun-7-backend-core](fun-7-backend-core) 
would have an ingres while the other 3 would communicate using the internal Kubernetes cluster IP addresses assigned to the respective k8s **service** components.  
External calls to the 4 microservices can be done using their ingres IP addresses.  
All their Swagger UIs can be accessed by calling **http://<INGRES_IP_ADDRESS>/swagger-ui/index.html**  
> **Note:** make sure to replace the <INGRES_IP_ADDRESS> placeholder with the actual IP address of the microservice you are testing.

## Local run
To run the 4 microservices locally here are a few things that are needed after building them in a workspace:
 - [ads](ads) microservice:
   - update [application.properties](ads/src/main/resources/application.properties) by setting the respective values for the properties starting with **service.ads.adPartner**
   - run [com.zlatkosh.ads.AdsApplication](ads/src/main/java/com/zlatkosh/ads/AdsApplication.java)
 - [customer-support](customer-support) no changes are needed, just run [com.zlatkosh.customersupport.CustomerSupportApplication](customer-support/src/main/java/com/zlatkosh/customersupport/CustomerSupportApplication.java)
 - [multiplayer](multiplayer) no changes are needed, just run [com.zlatkosh.multiplayer.MultiplayerApplication](multiplayer/src/main/java/com/zlatkosh/multiplayer/MultiplayerApplication.java)
 - [fun-7-backend-core](fun-7-backend-core)
   - the runnable class is [com.zlatkosh.ServerApplication](fun-7-backend-core/server/src/main/java/com/zlatkosh/ServerApplication.java)
   - update the Postgres datasource properties (jdbc-url, username and password) in [DB_datasource.properties](fun-7-backend-core/data-access/src/main/resources/DB_datasource.properties)