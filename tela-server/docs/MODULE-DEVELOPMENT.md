# Developing a Tela module

This document covers the process of developing and building a module, extending the functionality of Tela.

## Prerequisites 

This tutorial assumes that Tela has already been installed via Maven, or that we are directly coding in the original source code.

In order to test the module we will use the tool Tela CLI, connecting to the local server we will be developing:

```bash
npm install -g tela-cli
tela-cli connect localhost 8080
```

## Style Conventions

### File structure

This is the recommended file structure:

```
.
└── [Module name]
    ├── [Module name]TelaModule.java
    ├── actions
    │   └── (Action classes)
    ├── api
    │   ├── (Classes interacting with external APIs)
    │   ├── exceptions
    │   │   └── (API exceptions)
    │   ├── models
    │   │   └── (API-specific models)
    │   └── responses
    │       └── (Response models from the external API)
    ├── cache
    │   └── (Cache implementations)
    ├── databases
    │   └── extensions
    │       └── (Database extensions)
    ├── models
    │   └── (Module models)
    └── repositories
        └── (Repositories)

```


## Developing the Module

### Creating a Basic Module

Let's create a new package called `tutorial` for our module. Inside, we will create a `TutorialTelaModule` class, extending `TelaModule`. It will simply invoke the parent constructor, passing its name.

```java
package io.reneses.tela.modules.tutorial;
import io.reneses.tela.modules.TelaModule;

public class TutorialTelaModule extends TelaModule {
    public static final String NAME = "tutorial";
    public TutorialTelaModule() {
        super(NAME);
    }
}
```

Then, we will assemble the server registering the created module: 

```java
package io.reneses.tela;
import io.reneses.tela.modules.tutorial.TutorialTelaModule;

public class App {
    public static void main(String[] args) {
        Configuration config = ConfigurationFactory.create();
        config.setProperty(Configuration.Property.SCHEDULER_DELAY, 5);
        config.setProperty(Configuration.Property.ORIENTDB_MODE, Configuration.OrientDbMode.MEMORY);
        Assembler.build(config, new TutorialTelaModule()).start();
    }
}
```

*Note: in this code we have configure a short scheduler delay and the temporary mode for the database. This is not needed so far, but will be used in the future.*

### Adding our First 'Hello World' Action

Let's add a simple action to the module. First, create the file `modules/tutorial/actions/Actions.java` and copy the following:

```java
package io.reneses.tela.modules.tutorial.actions;

import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.core.dispatcher.annotations.Schedulable;

@Module("tutorial")
public class Actions {
    
    @Action(parameters = {})
    public String hello() {
        return "Hello World";
    }
    
}
```

If we execute the main method, the server will output at the console:

```
DEBUG	Action loaded: tutorial/hello [] 
```

And we will able to test it with Tela CLI:

```bash
> tela-cli execute tutorial hello
Hello World! 
```

### Adding a Parameter to the 'Hello World' Action

We will create another hello method admitting a parameter:

```java
@Action(parameters = {"name"})
public String hello(String name) {
    return "Hello " + name + "!";
}
```

If we execute the main method, the server will output at the console:

```
18:12:34	DEBUG	Action loaded: tutorial/hello [] 
18:12:34	DEBUG	Action loaded: tutorial/hello [name] 
```

And we will able to test it with Tela CLI:

```bash
> tela-cli execute tutorial hello name=Josh
Hello Josh! 
```

### Adding an Integer Parameter and Specifying an Action Name

Parameters are automatically casted, we just have to create a method with it and include it in the `@Action` annotation:

```java
@Action(name = "hello", parameters = {"name", "age"})
public String helloWithAge(String name, int age) {
  return "Hello " + name + " of " + age + " years!";
}
```

By default, Tela uses the method name as action name. If we want to specify another value, we can do it with the `name` property of the `@Action` annotation.

Now we can restart the server and try the new method:

```bash
> tela-cli execute tutorial hello name=Josh age=10
Hello Josh of 10 years!
```

### Adding an Array Parameter 

Parameters can also be arrays. In order to assign a name to the corresponding parameters, single nouns are recommended, as the URL syntax for an array is `?key=value1&key=value2&key=value3`. 

```java
@Action(name = "hello", parameters = {"friend"})
public String helloFriends(String[] name) {
    return "Hello " + String.join(", ", (CharSequence[]) name) + "!";
}
```
Now we can restart the server and try the new method:

```bash
> tela-cli execute tutorial hello friend=Josh friend=Peter friend=Mark
Hello Josh, Peter, Mark!

```

### Adding a Description and Generating Help 

The `description` property of the `@Action` annotation is useful in order to describe what the action performs. Let's annotate our actions:

```java
@Action(description = "Simple hello", parameters = {})
public String hello() {
    return "Hello World!";
}

@Action(description = "Hello with name", parameters = {"name"})
public String hello(String name) {
    return "Hello " + name + "!";
}

@Action(name = "hello", description = "Hello with name and age",  parameters = {"name", "age"})
public String helloFriends(String name, int age) {
    return "Hello " + name + " of " + age + " years!";
}

@Action(name = "hello", description = "Hello to all your friends!", parameters = {"friend"})
public String helloFriends(String[] name) {
    return "Hello " + String.join(", ", (CharSequence[]) name) + "!";
}
```

Now, if we retrieve the help:

```bash
> tela-cli help
tutorial/hello
 - Description:  Hello with name
 - Parameters: name: String
tutorial/hello
 - Description:  Simple hello
tutorial/hello
 - Description:  Hello with name and age
 - Parameters: name: String, age: int
tutorial/hello
 - Description:  Hello to all your friends!
 - Parameters: friend: String[]
```

### Adding Action Scheduling

Scheduling functionality can be added to an action using the `@Schedulable` annotation, along with the `minimumDelay` property (in seconds).

Let's create a schedulable method:

```java
@Action(name = "hello-date", description = "Hello with date!", parameters = {})
@Schedulable(minimumDelay = 1)
public String helloDate() {
    return "Hello again! (At: " + new Date() + ")";
}
```

And execute it:

```bash
> execute tutorial hello-date
Hello again! (At: Wed Nov 02 13:44:42 CET 2016)
> tela-cli schedule 1 tutorial hello-date | jq
{
  "scheduledAction": {
    "id": 280354576,
    "delay": 1,
    "accessToken": "34grs87g65ebhj08v1in3sg39l",
    "params": {},
    "createdAt": 1478090696296,
    "nextExecution": 1478090697296,
    "module": "tutorial",
    "action": "hello-date"
  },
  "result": "Hello again! (At: Wed Nov 02 13:44:56 CET 2016)"
}
```

In the server logs we can see how the scheduled action was actually executed:

```bash
13:44:47	DEBUG	Scheduler executed 
13:44:52	DEBUG	Scheduler executed 
13:44:56	INFO	[Scheduler] Scheduled action: tutorial/hello-date (1s) [] with ID 280354576, next: 2016-11-02T13:44:57.296 
13:44:56	INFO	127.0.0.1:tpic6pskdg5ptmklftqljb10ch:34grs87g65ebhj08v1in3sg39l [Schedule] tutorial/hello-date each 1s 
13:44:57	DEBUG	Scheduler executed 
13:44:57	INFO	[Scheduler] Executing scheduled task: tutorial/hello-date (1s) [] with ID 280354576, next: 2016-11-02T13:44:57.296 
13:45:02	DEBUG	Scheduler executed 
13:45:02	INFO	[Scheduler] Executing scheduled task: tutorial/hello-date (1s) [] with ID 280354576, next: 2016-11-02T13:44:58.296 
13:45:07	DEBUG	Scheduler executed 
13:45:07	INFO	[Scheduler] Executing scheduled task: tutorial/hello-date (1s) [] with ID 280354576, next: 2016-11-02T13:45:03.296 
13:45:09	INFO	127.0.0.1:tpic6pskdg5ptmklftqljb10ch:13:45:17	DEBUG	Scheduler executed 
```