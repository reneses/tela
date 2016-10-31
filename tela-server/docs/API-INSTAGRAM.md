# Tela Instagram API

This manual documents all the endpoints of the API of the Instagram Tela Module.

## Response models

The Tela Instagram API results completely follows the models of the ones provided by the Official Instagram API, which can be seen on their [API Endpoints documentation](https://www.instagram.com/developer/endpoints/). Therefore, an existing application that uses the Instagram API can be directly switched to using Tela without major changes.

## Authorization

Some of the endpoints we will describe in this document require users to authorize themselves at the moment of the request with a Tela Access Token, with an Instagram Module Token associated to it. This is done using the `Authorization` header, along with a `Bearer` token:

#### Request Headers

Header        |	Examle                               | Required
:------------:|:-------------------------------------:|:--------:
Authorization	 | Authorization: Bearer \<access_token> | Yes

#### Obtaining a Instagram Module Token

In order to retrieve data from the official Instagram API, an Instagram access token (an Instagram module token on Tela) is required. 

This is responsability of each client app, so that they are able to implement and integrate the authorization process as they want. You can read more about that in the official [Instagram documentation](https://www.instagram.com/developer/), particularly in the [client management](https://www.instagram.com/developer/clients/manage/) and [auth flow](https://www.instagram.com/developer/authentication/) sections.

Important: by default, new applications are created in [Sandbox Mode](https://www.instagram.com/developer/sandbox/) until review, and therefore the results of the API will be limited to Sandbox Users.

#### Creating a Tela Session with the Instagram Module Token

The process of creating a session and obtaining a bearer access token, as well as associating module tokens to it, is covered in the [documentation of the API of the core](./API.md). 

## User Actions

Actions related with information retrieval of users.

### Self

```
GET /instagram/self
```
- **Description**: Get the information about the authorized user. 
- **Requires authorization**: Yes.
- **Output**: Full information about the logged user.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: No.

#### Examples

```
GET /instagram/self
```
```json
{ 
  username: 'snoopdogg',
  bio: 'Snoop Snoop Snoop!',
  website: 'http://reneses.io',
  id: 1574083,
  profile_picture: 'https://scontent.cdninstagram.com/snoop.jpg',
  full_name: 'Snoop Dogg',
  counts: { 
    media: 412,
    followed_by: 822,
    follows: 420,
    created_at: 1477910384251
  }
}
```

### Search

```
GET /instagram/search
```
- **Description**: Search a user by its username. 
- **Requires authorization**: Yes.
- **Output**: Basic information about the user with the given username.
- **Schedulable**: No.
- **Requires Instagram Scope**: `public_content`.

#### Parameters
Name     | Type   | Description                               | Required
:-------:|:------:|:-----------------------------------------:|:---------:
username | string | Username to search token we are supplying | Yes

#### Examples

```
GET /instagram/search?username=snoopdogg
```
```json
{ 
  username: 'snoopdogg',
  id: 1574083,
  profile_picture: 'https://scontent.cdninstagram.com/snoop.jpg',
  full_name: 'Snoop Dogg'
}
```

### User

```
GET /instagram/user
```
- **Description**: Get the information about a user. 
- **Requires authorization**: Yes.
- **Output**: Full information about a user.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `public_content`.

#### Parameters
Name     | Type   | Description                               | Required
:-------:|:------:|:-----------------------------------------:|:---------:
userId   | long   | ID of the user to retrieve                | If not username
username | string | Username of the user to retrieve          | If not userId

#### Examples

The two following requests are equivalent:

```
GET /instagram/user?userId=1574083
GET /instagram/user?username=snoopdogg
```

Producing: 

```json
{ 
  username: 'snoopdogg',
  bio: 'Snoop Snoop Snoop!',
  website: 'http://reneses.io',
  id: 1574083,
  profile_picture: 'https://scontent.cdninstagram.com/snoop.jpg',
  full_name: 'Snoop Dogg',
  counts: { 
    media: 412,
    followed_by: 822,
    follows: 420,
    created_at: 1477910384251
  }
}
```

### Counts

```
GET /instagram/counts
```
- **Description**: Get the retrieved counts of a user. 
- **Requires authorization**: Yes.
- **Output**: Counts (number of media, followers and following) of a user.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `public_content`.

#### Parameters
Name     | Type   | Description                               | Required
:-------:|:------:|:-----------------------------------------:|:---------:
userId   | long   | ID of the user to retrieve                | If not username
username | string | Username of the user to retrieve          | If not userId

#### Examples

The two following requests are equivalent:

```
GET /instagram/counts?userId=1574083
GET /instagram/counts?username=snoopdogg
```

Producing: 

```json
[ 
  { 
    media: 400,
    followed_by: 800,
    follows: 419,
    created_at: 1477400384256
  },
  { 
    media: 412,
    followed_by: 822,
    follows: 420,
    created_at: 1477910384251
  }
]
```


### Following

```
GET /instagram/following
```
- **Description**: Get the users the authorized user is following. 
- **Requires authorization**: Yes.
- **Output**: Basic information of the users the authorized user is following.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `follower_list`.

#### Parameters
Name     | Type    | Description                             | Required
:-------:|:-------:|:---------------------------------------:|:---------:
limit    | int     | Number of users to retrieve             | No

#### Examples

```
GET /instagram/following?limit=1
```
```json
[
  { 
    username: 'snoopdogg',
    id: 1574083,
    profile_picture: 'https://scontent.cdninstagram.com/snoop.jpg',
    full_name: 'Snoop Dogg',
  }
]
```

### Followers

```
GET /instagram/followers
```
- **Description**: Get the followers of the authorized user. 
- **Requires authorization**: Yes.
- **Output**: Basic information of the followers of the authorized user.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `follower_list`.

#### Parameters
Name     | Type    | Description                             | Required
:-------:|:-------:|:---------------------------------------:|:---------:
limit    | int     | Number of users to retrieve             | No

#### Examples

```
GET /instagram/followers?limit=1
```
```json
[
  { 
    username: 'snoopdogg',
    id: 1574083,
    profile_picture: 'https://scontent.cdninstagram.com/snoop.jpg',
    full_name: 'Snoop Dogg',
  }
]
```

### Friends

```
GET /instagram/friends
```
- **Description**: Get the friends (intersection of folowers and following) of the authorized user. 
- **Requires authorization**: Yes.
- **Output**: Basic information of the friends of the authorized user.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `follower_list`.

#### Examples

```
GET /instagram/friends
```
```json
[
  { 
    username: 'snoopdogg',
    id: 1574083,
    profile_picture: 'https://scontent.cdninstagram.com/snoop.jpg',
    full_name: 'Snoop Dogg',
  }
]
```

### Relationship

```
GET /instagram/relationship
```
- **Description**: Get the relationship between the authorized user and a user. 
- **Requires authorization**: Yes.
- **Output**: Relationship.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `follower_list `.

#### Parameters
Name     | Type   | Description                               | Required
:-------:|:------:|:-----------------------------------------:|:---------:
userId   | long   | ID of the other user                      | If not username
username | string | Username of the other user                | If not userId

#### Response values

- **Outgoing**: `follows`, `requested`, `none`.
- **Incoming**: `followed_by`, `requested_by`, `blocked_by_you` and `none`. 

#### Examples

The two following requests are equivalent:

```
GET /instagram/relationship?userId=1574083
GET /instagram/relationship?username=snoopdogg
```

Producing: 

```json
{ 
  "outgoing_status": "follows",
  "incoming_status": "followed_by",
  "target_user_is_private": false
}
```
