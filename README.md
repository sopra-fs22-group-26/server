
<div align="center">
    <h2>SoPra FS22 - Group 26 Server</h2>
</div>
<p align="center">
<img src="https://github.com/sopra-fs22-group-26/client/blob/main/src/images/scrumblebee_logo_508x95.png?raw=true" width="508" height="95" />
</p>



# Scrumble(:bee:)
## Introduction
The idea of our project ScrumbleBee is to create an online task management system which encourages its users to complete their tasks by including gamification elements. In addition to the basic capabilities, like assigning users, due dates and comments to tasks, the members of ScrumbleBee can award points to each other for having successfully completed tasks - which peaks in a fiercely battle for the number one spot on the scoreboard!
. As this platform could also be used in a Scrum environment, we include an	 “Estimate Poll” widget, where the members can estimate the time needed to complete a task in real-time. By considering information from external sources like google maps API location or dates of public holidays, time collisions should belong to the past, and a calendar export function allows the product to be integrated in your daily workflow.

## Technologies

- Java 
- SpringBoot
- JPA (with H2 database)
- Github Actions
- Heroku
- REST API

## High-Level Components

The most important components are:
- blabnla
- alsdfnalsdfn


## Launch & Deployment
how to launch eervasdfasdjkf




## Roadmap
Potential improvements or extensions in the future may include:

- Implement a group feature, such that users are able to create and join subgroups. E.g. a user is part of a work group and part of a family group.
- Since ScrumbleBee is not a round-based game it is critical than you can log in with the same credentials everytime. A user doesn't want to lose all his scoreboard points just because they forgot the password. So a "Forgot Password" feature could be a next improvement.
- To further develope the calender integration, one could also connect a company or private calender to not only prevent national holiday conflicts but also personal or company ones.

### Postman

-   If you plan to add new API Endpoints to the application, we highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.
## Learn More

- Spring Documentation [Spring Documentation](https://spring.io/guides/gs/spring-boot/)
- Getting started with Java [Java Documentation](https://docs.oracle.com/en/java/)
- SpringBoot Testing: [SpringBoot testing](https://www.baeldung.com/spring-boot-testing/)


## Authors & Acknowledement
>J. Zellweger, R. Hany, W. Chang & N. Mantzanas

>SoPra Team for the template and our TA T. Alakmeh

## License

Licensed under GNU General Public License v3.0
- See [License](LICENSE)







# SoPra FS22 - Group 26
## ScrumbleBee

## Getting started with Spring Boot

-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: http://spring.io/guides/tutorials/bookmarks/

## Setup this Template with your IDE of choice

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)), [Visual Studio Code](https://code.visualstudio.com/) and make sure Java 15 is installed on your system (for Windows-users, please make sure your JAVA_HOME environment variable is set to the correct version of Java).

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions will help you to run it more easily:
-   `pivotal.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`
-   `richardwillis.vscode-gradle`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs22` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle

You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing

### Postman

-   We highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.

## Debugging

If something is not working and/or you don't know what is going on. We highly recommend that you use a debugger and step
through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command),
do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug"Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

