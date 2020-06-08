# guardiansRESTinterface
This repository implements the REST interface provided by the guardians 
service.

Currently, the first use case has been implemented. This is, allowing 
to generate the schedule for a certain month.

## Understanding the service
The explanation of the service as a whole can be found [here](TODO).

The documentation of the REST interface can be found [here](https://miggoncan.github.io/guardiansRESTinterfaceDoc).

The scheduler repository can be found [here](https://github.com/miggoncan/guardiansScheduler).

## Development
To work on this project, there are mainly four steps to be taken:
1. Install Java 1.8
2. Install Lombok and enable its anotation preprocessing in the desired IDE
3. Configure the database
4. Configure the IDE
5. Configure the integration with the scheduler

Further instructions on the first four steps can be found [here](https://github.com/miggoncan/guardiansRESTinterfaceDoc/blob/master/setup/setup.md).

### Configure integration with the scheduler
1. Follow the setup instructions for the scheduler. Found [here](TODO).
2. Configure the `resource/application.properties`:
    1. Change the property `scheduler.command` to be the path to a 
    python interpreter, version 3.7+. For example, `python3.7` 
    (supossing the binary is in the PATH)
    2. Change the property `scheduler.entryPoint` to the path to the 
    `src/main.py` file of the scheduler. For example, if the scheduler 
    repository was in `/home/itt/Documents/scheduler`, the property 
    should have the value `/home/itt/Documents/scheduler/src/main.py`
    3. The properties `scheduler.file.*` will be the temporary files 
    used for communication between the REST service and the scheduler.
    Make sure both of them have read and write privileges on these files
    4. [Optional] The property `scheduler.timeout` is an integer that 
    will indicate the number of minutes to wait before killing the 
    scheduler process and considering the schedule generation failed 
    (after a request to generate a schedule)

## Production
To get a production version of this project, there are two steps to be taken:
1. Change all the default passwords defined in resources/application.properties
2. Create a new keypair fot SSL, and move it to resources/keystore/guardiansREST.p12

To generate this keypair, we can use `keytool`, as it is shipped with the Java Runtime Environment:
```
keytool -genkeypair -alias guardians -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore guardiansREST.p12 -validity 3650 
```
And then, we have to move the generated key to the said location.
