
<div align="center">
    <h2>SoPra FS22 - Group 26 Server</h2>
</div>
<p align="center">
<img src="https://github.com/sopra-fs22-group-26/client/blob/main/src/images/scrumblebee_logo_508x95.png?raw=true" width="508" height="95" />
</p>



# Scrumble(:bee:)
## Introduction
The idea of our project ScrumbleBee is to create an online task management system which encourages its users to complete their tasks by including gamification elements. In addition to the basic capabilities, like assigning users, due dates and comments to tasks, the members of ScrumbleBee can award points to each other for having successfully completed tasks - which peaks in a fiercely battle for the number one spot on the scoreboard!
As this platform could also be used in a Scrum environment, we include an	 “Estimate Poll” widget, where the members can estimate the time needed to complete a task in real-time. By considering information from external sources like google maps API location or dates of public holidays, time collisions should belong to the past, and a calendar export function allows the product to be integrated in your daily workflow.

## Technologies

- Java 
- SpringBoot
- JPA (with H2 database)
- Github Actions
- Heroku
- REST API

## High-Level Components

The most important components are:
- [User](src/main/java/ch/uzh/ifi/group26/scrumblebee/entity/User.java)
- [Task](src/main/java/ch/uzh/ifi/group26/scrumblebee/entity/Task.java)
- [Poll Meeting](src/main/java/ch/uzh/ifi/group26/scrumblebee/entity/PollMeeting.java)

### User
The user is one of the most important component in ScrumbleBee. A user can create, rate or change a task. When a user completes a task, they can climb up the scoreboard! The user entity is also used to create the [PollParticipant](src/main/java/ch/uzh/ifi/group26/scrumblebee/entity/PollParticipant.java)
in order to take part in poll meetings.

### Task
Besides the user the task entity is the heart of any task-management system. It also has a strong connection to the [Comment](src/main/java/ch/uzh/ifi/group26/scrumblebee/entity/Comment.java)
class, since users can post comments on any task.

### Poll Meeting

When a user is not sure about the time estimate for a specific task, they can start a poll session. The user invites other users (then called poll participants) and each one of them can 
give their guess on how long the task is going to take. The average of all guesses will be set as time estimate.


## Launch & Deployment

To build the application you can use the local Gradle Wrapper.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

After that, you can build, run  and test the application with the following commands:

 

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
**Note**: _Don't forget to update/reset the frontend URL_
### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`





## Roadmap
Potential improvements or extensions in the future may include:

- Implement a group feature, such that users are able to create and join subgroups. E.g. a user is part of a work group and part of a family group.
- Since ScrumbleBee is not a round-based game it is critical than you can log in with the same credentials everytime. A user doesn't want to lose all his scoreboard points just because they forgot the password. So a "Forgot Password" feature could be a next improvement.
- To further develop the calendar integration, one could also connect a company or private calendar to not only prevent national holiday conflicts but also personal or company ones.

### Postman

- If you plan to add new API Endpoints to the application, we highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.
However, be aware that the REST Endpoints are secured by a Token authentication. We recommend turning this functionality off for development purposes.
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


