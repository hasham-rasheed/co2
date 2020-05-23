# CO2 Calculator - Command line program
Calculates total co2 emissions emitted by different sized vehicles traveling between 2 cities and 

### Built With

* [Maven](http://maven.apache.org/) - Dependency Management<br/>
* [JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html) - Java™ Platform, Standard Edition Development Kit
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications<br/>
* [Lombok](https://projectlombok.org/) - Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.<br/>
* [Git](https://git-scm.com/) - Free and Open-Source distributed version control system<br/>
* [Mockito](https://site.mockito.org/) - Open source testing framework for Java released under the MIT License

###Prerequisites
This application uses [openrouteservice](https://openrouteservice.org/) to get the distance between 2 cities. 
Please create a free account to get an API Token. You will need to save this API Token in an environment variable called `ORS_TOKEN`.
Alternatively, you can add this API token in `application.properties` with key `fallback.api.key`.

Apart from this, you will need to set up JDK 8 and Maven along with their environment variables.

### Building and running the application locally
####Build
You can build the application by simply running `mvn clean install`

####Running the application
There are several ways to run this application on your local machine. 
In any case, the program will require the following arguments at runtime:

* `--start = START_CITY`
* `--end = END_CITY`
* `--transportation-method = VEHICLE_TYPE`

Program arguments can be put in any order and either use a space ( ) or equal sign ( = ) between key and value.<br/>
Applicable values for `--transportation-method` are:

* Small Cars:
   * `small-diesel-car`
   * `small-petrol-car`
   * `small-plugin-hybrid-car`
   * `small-electric-car`
    
* Medium Cars:
   * `medium-diesel-car`
   * `medium-petrol-car`
   * `medium-plugin-hybrid-car`
   * `medium-electric-car`
   
* Large Cars:
   * `large-diesel-car`
   * `large-petrol-car`
   * `large-plugin-hybrid-car`
   * `large-electric-car`
   
* Bus:
   * `bus`
   
* Train:
   * `train`
   

1. One way is to execute the `main` method in the `com.app.co2.Co2Application` class from your IDE ([Eclipse](https://www.eclipse.org/) in this case):
   
   - Download the zip or clone the Git repository.
   - Unzip the zip file (if you downloaded one)
   - Open Command Prompt and Change directory (cd) to folder containing pom.xml
   - Open Eclipse 
      - File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
      - Select the project
   - Choose the Spring Boot Application file (search for @SpringBootApplication)
   - Add the runtime arguments mentioned above
   - Right Click on the file and Run as Java Application   
   
2. Second way is to use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:
    ```shell
    mvn spring-boot:run -Dspring-boot.run.arguments="--start=START_CITY, --end=END_CITY, --transportation-method=VEHICLE_TYPE"
    ```
3. Lastly, you can run via command prompt / terminal:
    ```shell
     java -jar PATH_TO_TARGET_FOLDER/co2-1.0.jar --start = START_CITY --end = END_CITY --transportation-method = VEHICLE_TYPE
    ```
Where `START_CITY`, `END_CITY` and `VEHICLE_TYPE` are placeholders for program arguments and `PATH_TO_TARGET_FOLDER` is a placeholder for the actual path of this project's target folder.




