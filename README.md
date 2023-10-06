# TriggerService


#### REST API which using Spring for triggering builds.

+ The function of triggering builds performs in two ways
    * according to a schedule
    * upon a repository change event (appearance of a new commit)


Prerequisites:
* Docker (can be download https://www.docker.com/products/docker-desktop)

Run instructions:
* clone app
* generate github API token from your accoutn and copy it to application.properties file
    + github.api.token=#yourtoken_in_base64
* run rabbitmq
  * you can do that with a docker, here is a sample 
  
  `docker run -d --hostname my-rabbit --name my-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management`
* run postgresql(also can be done by docker)
* create db/table by running following queries
      * ` 
  
       CREATE DATABASE buildtrigger;
      
       USE buildtrigger;
          
       CREATE TABLE job_history (
    
        id SERIAL PRIMARY KEY,
    
        repository_url VARCHAR(500) NOT NULL,
    
        branch VARCHAR(50) NOT NULL,
    
        start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    
        end_time TIMESTAMP WITHOUT TIME ZONE,
    
        triggered_by VARCHAR(100) NOT NULL,
    
        build_status VARCHAR(50),
    
        logs TEXT
        );
  `

