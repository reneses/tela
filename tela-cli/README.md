# TELA CLI

Tela CLI is eases interaction with a Tela Server from the command line.

## Installation

### NPM

**Tela CLI** is a **Node** application and can be installed directly from [**npm**](https://www.npmjs.com/package/tela-cli):

```bash
npm install -g tela-cli
```

Then, you should be able to execute:

```bash
tela-cli --help
```

### Manual Installation

Assuming that we are inside the folder of the application, we just have to install the project with npm:

```bash
npm install
```

Then, we are ready to use the CLI tool:

```bash
node /bin/cli --help
```


## Connection to a Tela Server

### Connection

In order to create a connection with our Tela Server, execute:

```bash
tela-cli connect <host> [<port>]
``` 

For example, using the public server:

```bash
tela-cli connect tela.herokuapp.com
```

This command will create a session in the server, and write it into a `.tela` file, which stores the details of the connection.

### Disconnection

Disconnection from the Tela Server can be achieve by executing:

```bash
tela-cli disconnect
```

This will delete the session from the server, as well as all its associated tokens.

## Executing Actions

Actions can be executed with the `execute` command:

```bash
tela-cli execute <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]
```

The result is printed into the system output, encoded as JSON. This allows to use the Tela CLI along with console commands. For example, save the results into a file: 

```bash
tela-cli execute <module> <action> > result.txt
```

Or format the JSON output:

```bash
tela-cli execute <module> <action> | jq
```

## Scheduling

### Schedule an Action

In order to schedule an action, execute the `schedule` command, followed by the delay of execution (in seconds) and the same arguments than the `execute` command:

```bash
tela-cli schedule <delay> <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]
```

The information of the scheduled task, as well as the result of its execution, will be outputted to the console.

### Get the Scheduled Actions

It is possible to retrieve all the actions that the authorized user has scheduled, by running:

```bash
tela-cli scheduled
```

### Cancel Scheduled Action(s)

It is possible to stop the scheduled execution of an action using the `cancel` command:

```bash
tela-cli cancel (<scheduled_action_id> | all)
```

If an ID is provided, only that action will be cancelled. In case `all` is supplied, all the actions scheduled by the authorized user will be cancelled.

## Help

To obtain all the available modules and actions configured in the Tela Server we are connected to, execute:


```bash
tela-cli help
```

Or, just for a certain module:

```bash
tela-cli help <module>
```

*Note: the `help` command returns all the modules and actions configured within the Tela Server. Some of them might not be supported in this CLI tool.*



## Modules 

### Configuring Modules

Modules might have specific properties which can be configured, by executing: 

```bash
tela-cli configure <module> <property> <value>
```

### Linking & Unlinking Modules

Linking a module is the process of obtaining an access token for it, and storing it in our Tela session. This process is only needed if the module requires a token.

```bash
tela-cli link <module>
```

On the other hand, the `unlink` command deletes the module token from the session:

```bash
tela-cli unlink <module>
```

### Supported Modules

#### Instagram

The Instagram module requires the configuration properties `clientId` and `clientSecret`, which can be obtained creating an [Instagram Application](https://www.instagram.com/developer/clients/manage/).

##### Setting Up

```bash
tela-cli connect <host> <port>
tela-cli configure instagram clientId <client_id>
tela-cli configure instagram clientSecret <client_secret>
tela-cli link instagram
```

##### Executing Actions

Once the Instagram module has been set up, you can execute actions with:

```bash
tela-cli execute instagram <action> [<param1>=<value1> [<param2>=<value2> ...]]
```

For example:

```bash
# Obtain info about the logged user
tela-cli execute instagram self

# Search a user
tela-cli execute instagram user username=themedizine
```