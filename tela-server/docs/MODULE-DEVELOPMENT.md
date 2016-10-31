# Developing a Tela module

This document covers the process of developing and building a module, extending the functionality of Tela.

## Prerequisites 

This tutorial assumes that Tela has already been installed via Maven, or that we are directly coding in the original source code.

## Style Conventions

### Package name

The recommended package for module development is `io.reneses.tela.modules`.

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


## Creating the module

Our new module will be called `tutorial`, so our package will be `io.reneses.tela.modules.tutorial`.

Inside, we will create a `TutorialTelaModule` class, extending `TelaModule`. It will simply invoke the parent constructor, passing its name.

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
        Assembler.build(new TutorialTelaModule()).start();
    }
}
```

## Adding our first 'Hello World' action

Let's add a simple action to the module. First, create the file `modules/tutorial/actions/Actions.java` and copy the following:

```java
package io.reneses.tela.modules.tutorial.actions;

import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.core.dispatcher.annotations.Schedulable;

@Module("tutorial")
public class Actions {
    
    @Action(name = "hello", description = "Test", parameters = {})
    @Schedulable(minimumDelay = 3600)
    public String hello() {
        return "Hello World";
    }
    
}
```

If we execute the main method, we should read at the console:

```
DEBUG	Scanning classes: [class io.reneses.tela.modules.tutorial.actions.Actions, class io.reneses.tela.modules.tutorial.TutorialTelaModule] 
DEBUG	Action loaded: tutorial/hello [] 
```

Now, if we create a Tela Session and execute a GET request to `http://127.0.0.1:8080/action/tutorial/hello`, we will obtain `Hello World!`.