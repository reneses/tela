# TELA API

This manual documents all the endpoints of the API of the core of Tela.

## Authentication

Some of the endpoints we will describe in this document require users to authorize themselves at the moment of the request. This is done using the `Authorization` header, along with a `Bearer` token:

#### Request Headers

Header        |	Examle                               | Required
:------------:|:-------------------------------------:|:--------:
Authorization	 | Authorization: Bearer \<access_token> | Yes

The process of obtaining an `access_token` will be covered in the following section.

## Auth API

The Auth API is in charge of creating, updating and deleting Tela sessions.

### Create a Session

```
POST /auth
```
- **Description**: Create a session and send its access token back to the user. If the `module`and `token` parameters are supplied, it will directly store the given module token within the created session (shortcut for create + add token). 
- **Requires authentication**: No.
- **Output**: Tela access token.

#### Parameters
Name   | Type   | Description                               | Required
:-----:|:------:|:-----------------------------------------:|:---------:
module | string | Module name of the token we are supplying | If token
token  | string | Access token for the module               | If module

#### Examples

Create asession:

```
POST /auth
```

```json
"123456789"
```

Create a session with a module token:

```
POST /auth?module=instagram&token=12345
```
```json
"987654321"
```


### Delete a session

```
DELETE /auth
```

- **Description**: Delete a session, as well as all the module tokens stores with it.
- **Requires authentication**: Yes.
- **Output**: Success message.

#### Example

```
DELETE /auth
```
```json
"Success"
```


### Add a Module Token

```
POST /auth/{module}
```
- **Description**: Add a module token to the current session.
- **Requires authentication**: Yes.
- **Output**: Success message.

#### Parameters
Name   | Type   | Description                               | Required
:-----:|:------:|:-----------------------------------------:|:---------:
module | string | Module name of the token we are supplying | Yes
token  | string | Access token for the module               | Yes

#### Example

```
POST /auth/instagram?token=12345
```
```json
"Success"
```


### Delete a Session Module

```
DELETE /auth/{module}
```
- **Description:** Delete a specific module token.
- **Requires authentication:** Yes.
- **Output**: Success message.

#### Parameters
Name   | Type   | Description                               | Required
:-----:|:------:|:-----------------------------------------:|:---------:
module | string | Module name of the token we are supplying | Yes

#### Example
```
DELETE /auth/{module}
```
```json
"Success"
```

## General API

The General API offers a test endpoint, as well as help about the installed modules.

### Test the Server status

```
GET /test
```
- **Description**: Simple test endpoint. 
- **Requires authentication**: No.
- **Output**: OK message (in plain text).

#### Example Response

```
GET /test
```
```
OK
```

### Help about the Installed Modules and Actions

```
GET /help/{module}
```
- **Description**: Get the list of the available actions, as well as its information. 
- **Requires authentication**: No.
- **Output**: List of information about the actions of the provided module, or of all if none given.

#### Parameters
Name   | Type   | Description    | Required
:-----:|:------:|:--------------:|:---------:
module | string | Module name    | No

#### Examples

Help of all modules:

```
GET /help
```
```json
[
  {
    "module": "twitter",
    "name": "self",
    "params": [
      "token: String"
    ],
    "description": "Get the information about the logged user"
  },
  {
    "module": "instagram",
    "name": "user",
    "params": [
      "token: String",
      "userId: long"
    ],
    "description": "Get the information about a user"
  }
]
```

Help of a given module:

```
GET /help/instagram
```
```json
[
  {
    "module": "instagram",
    "name": "self",
    "params": [
      "token: String"
    ],
    "description": "Get the information about the logged user"
  }
]
```


## Actions API

### Execute an Action

```
GET /action/{module}/{action}
```
- **Description**: Execute an action. 
- **Requires authentication**: Yes.
- **Output**: Result of the action.

#### Parameters

This endpoint accepts a variable number of parameters, corresponding to the parameters of the action to be executed:

```
GET /action/{module}/{action}?param1=value1&param2=value2&...
```

Name   | Type   | Description    | Required
:-----:|:------:|:--------------:|:---------:
module | string | Module name    | Yes
action | string | Action name    | Yes
_{param1}_ | _type1_  | Action param   | No
_{param2}_ | _type2_  | Action param   | No
...    | ...    | ...            | No



#### Example

Instagram self:

```
GET /action/instagram/user?username=snoopdogg
```
```json
{
  "username": "snoopdogg",
  "profile_picture": "snoop.jpg",
  "id": 1574083,
  "full_name": "Snoop Dogg"
}
```

Instagram followers:

```
GET /action/instagram/followers?username=themedizine&limit=1
```
```json
{
  "username": "snoopdogg",
  "profile_picture": "snoop.jpg",
  "id": 1574083,
  "full_name": "Snoop Dogg"
}
```

## Scheduler API

The Scheduler API offers scheduling functionality.

### Schedule

```
GET /schedule/{module}/{action}
```
- **Description**: Schedule an action. 
- **Requires authentication**: Yes.
- **Output**: Scheduled action and result of the action.

#### Parameters

This endpoint accepts a variable number of parameters, corresponding to the parameters of the action to be executed:

```
GET /action/{module}/{action}?delay=5&param1=value1&param2=value2&...
```

Name   | Type   | Description    | Required
:-----:|:------:|:--------------:|:---------:
module | string | Module name    | Yes
action | string | Action name    | Yes
delay  | number | Execution delay in seconds | No
_{param1}_ | _type1_  | Action param   | No
_{param2}_ | _type2_  | Action param   | No
...    | ...    | ...            | No



#### Example

```
GET /schedule/instagram/user?delay=4000&username=snoopdogg
```
```json
{ 
  "scheduledAction": {
    "createdAt": 1477865603381,
    "nextExecution": 1477866603381,
    "delay": 4000,
    "module": "instagram",
    "action": "self",
    "params": {},
    "id": 523899944 
  },
  "result": { 
    "username": "snoopdogg",
    "profile_picture": "snoop.jpg",
    "id": 1574083,
    "full_name": "Snoop Dogg"
  }
}

```

### Get Scheduled Actions

```
GET /schedule
```
- **Description**: Get all the scheduled actions by the authorized user. 
- **Requires authentication**: Yes.
- **Output**: Scheduled actions.

#### Example

```
GET /schedule
```
```json
[
  {
    "createdAt": 1477865603381,
    "nextExecution": 1477866603381,
    "delay": 4000,
    "module": "instagram",
    "action": "self",
    "params": {},
    "id": 523899944 
  }
]
```

### Cancel a Scheduled Action

```
DELETE /schedule/{scheduled}
```
- **Description**: Cancel a scheduled action.
- **Requires authentication**: Yes.
- **Output**: Success message.

#### Parameters

Name      | Type   | Description         | Required
:--------:|:------:|:-------------------:|:---------:
scheduled | number | Scheduled action ID | Yes


#### Example

```
DELETE /schedule/523899944
```
```json
"Success"
```

### Cancel All the Scheduled Actions

```
DELETE /schedule
```
- **Description**: Cancel all the scheduled actions.
- **Requires authentication**: Yes.
- **Output**: Success message.

#### Example

```
DELETE /schedule
```
```json
"Success"
```