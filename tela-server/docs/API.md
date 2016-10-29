https://developer.github.com/v3/markdown/
http://docs.grafana.org/reference/http_api/
https://gist.github.com/iros/3426278


## Authorization

#### Request Headers

Header        |	Examle                               | Required
:------------:|:------------------------------------:|:--------:
Authorization	 | Authorization: Bearer *ACCESS_TOKEN* | Yes



## Auth

### Create a Session

```
POST /auth
```
- **Description**: Create a session and send its access token back to the user. If the `module`and `token` parameters are supplied, it will directly store the token within the created session. 
- **Requires authorization**: No.
- **Output**: Tela access token.

#### Parameters
Name   | Type   | Description                               | Required
:-----:|:------:|:-----------------------------------------:|:---------:
module | string | Module name of the token we are supplying | If token
token  | string | Access token for the module               | If module

#### Examples

##### Create session



```
POST /auth
```

```
{
  accessToken: "123456789"
}
```
##### Create session with a module token


```
POST /auth?module=instagram&token=12345
```

```
{
  accessToken: "6789"
}
```


### Delete a session

```
DELETE /auth
```

- **Description**: Delete a session, as well as all the module tokens stores within it.
- **Requires authorization**: Yes.
- **Output**: Success message.

#### Example

```
DELETE /auth
```
```
Success
```


### Add a Module Token

```
POST /auth/{module}
```
- **Description**: Add a module token to the current session.
- **Requires authorization**: Yes.
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
```
Success
```


### Delete a Session Module

```
DELETE /auth/{module}
```
- **Description:** Delete a specific module token.
- **Requires authorization:** Yes.
- **Output**: Success message.

#### Parameters
Name   | Type   | Description                               | Required
:-----:|:------:|:-----------------------------------------:|:---------:
module | string | Module name of the token we are supplying | Yes

#### Example
```
DELETE /auth/{module}
```
```
Success
```










## API

### Create a Session

```
GET /test
```
- **Description**: Simple test endpoint. 
- **Requires authorization**: No.
- **Output**: OK message.

#### Example Response

```
GET /test
```
```
OK
```

### Help

```
GET /help
```
- **Description**: Get the list of the available actions, as well as its information. 
- **Requires authorization**: No.
- **Output**: List of information about the actions.

#### Examples

```
GET /help
```
```
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

### Help

```
GET /help/{module}
```
- **Description**: Get the list of the available actions from a specific module, as well as its information. 
- **Requires authorization**: No.
- **Output**: List of information about the actions.

#### Parameters
Name   | Type   | Description    | Required
:-----:|:------:|:--------------:|:---------:
module | string | Module name    | Yes

#### Example

```
GET /help/instagram
```
```
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


## Actions

### Execute an Action

```
GET /action/{module}/{action}
```
- **Description**: Execute the action. 
- **Requires authorization**: Yes.
- **Output**: Result of the action.

#### Parameters
Name   | Type   | Description    | Required
:-----:|:------:|:--------------:|:---------:
module | string | Module name    | Yes
action | string | Action name    | Yes
_{param1}_ | _type1_  | Action param   | No
_{param2}_ | _type2_  | Action param   | No
...    | ...    | ...            | No


```
GET /action/{module}/{action}?param1=value1&param2=value2&...
```



#### Example

```
GET /action/instagram/user?username=snoopdogg
```
```
{
  "username": "snoopdogg",
  "profile_picture": "snoop.jpg",
  "id": "1574083",
  "full_name": "Snoop Dogg"
}
```