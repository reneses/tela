# Tela Twitter API

This manual documents all the endpoints of the API of the Twitter Tela Module.

## Response models

The Tela Twitter API results completely follows the models of the ones provided by the Official Twitter API, which can be seen on their [API Endpoints documentation](https://dev.twitter.com/rest/public). Therefore, an existing application that uses the Twitter API can be directly switched to using Tela without major changes.

## Authorization

Some of the endpoints we will describe in this document require users to authorize themselves at the moment of the request with a Tela Access Token, with a Twitter Module Token associated to it. This is done using the `Authorization` header, along with a `Bearer` token:

#### Request Headers

Header        |	Examle                               | Required
:------------:|:-------------------------------------:|:--------:
Authorization	 | Authorization: Bearer \<access_token> | Yes

#### Obtaining a Twitter Module Token

In order to retrieve data from the official Twitter API, a Twitter access token (a Twitter module token on Tela) is required. 

This is responsibility  of each client app, so that they are able to implement and integrate the authorization process as they want. You can read more about that in the official [Twitter documentation](https://dev.twitter.com/rest/public), particularly in the [client management](https://apps.twitter.com/app/new) and [auth flow](https://dev.twitter.com/oauth/application-only) sections.

#### Creating a Tela Session with the Twitter Module Token

The process of creating a session and obtaining a bearer access token, as well as associating module tokens to it, is covered in the [documentation of the API of the core](./API.md). 

## User Actions

Actions related with information retrieval of users.

### Self

```
GET /twitter/self
```
- **Description**: Get the information about the authorized user. 
- **Requires authorization**: Yes.
- **Output**: Full information about the logged user.
- **Schedulable**: Yes (minimum delay: 3600s).

#### Examples

```
GET /twitter/self
```
```json
{ 
  "screen_name": "snoopdogg",
  "url": "http://twitter.com/snoopdogg",
  "id": 1574083,
  "profile_image_url": "https://twitter.com/snoop.jpg",
  "full_name": "Snoop Dogg",
  "followers_count": 822,
  "friends_count": 420
}
```

### User

```
GET /twitter/user
```
- **Description**: Get the information about a user. 
- **Requires authorization**: Yes.
- **Output**: Full information about a user.
- **Schedulable**: Yes (minimum delay: 3600s).

#### Parameters
Name     | Type    | Description                             | Required
:-------:|:-------:|:---------------------------------------:|:---------:
username | String  | Username of the user to retrieve        | Yes


#### Examples

```
GET /twitter/user?username=snoopdogg
```
```json
{ 
  "screen_name": "snoopdogg",
  "url": "http://twitter.com/snoopdogg",
  "id": 1574083,
  "profile_image_url": "https://twitter.com/snoop.jpg",
  "full_name": "Snoop Dogg",
  "followers_count": 822,
  "friends_count": 420
}
```

### Following

```
GET /twitter/following
```
- **Description**: Get the users the authorized user is following. 
- **Requires authorization**: Yes.
- **Output**: Basic information of the users the authorized user is following.
- **Schedulable**: Yes (minimum delay: 3600s).

#### Parameters
Name     | Type    | Description                             | Required
:-------:|:-------:|:---------------------------------------:|:---------:
limit    | int     | Number of users to retrieve             | No

#### Examples

```
GET /twitter/following?limit=1
```
```json
[
  { 
    "screen_name": "snoopdogg",
    "url": "http://twitter.com/snoopdogg",
    "id": 1574083,
    "profile_image_url": "https://twitter.com/snoop.jpg",
    "full_name": "Snoop Dogg",
    "followers_count": 822,
    "friends_count": 420
  }
]
```

### Followers

```
GET /twitter/followers
```
- **Description**: Get the followers of the authorized user. 
- **Requires authorization**: Yes.
- **Output**: Basic information of the followers of the authorized user.
- **Schedulable**: Yes (minimum delay: 3600s).

#### Parameters
Name     | Type    | Description                             | Required
:-------:|:-------:|:---------------------------------------:|:---------:
limit    | int     | Number of users to retrieve             | No

#### Examples

```
GET /twitter/followers?limit=1
```
```json
[
  { 
    "screen_name": "snoopdogg",
    "url": "http://twitter.com/snoopdogg",
    "id": 1574083,
    "profile_image_url": "https://twitter.com/snoop.jpg",
    "full_name": "Snoop Dogg",
    "followers_count": 822,
    "friends_count": 420
  }
]
```

### Friends

```
GET /twitter/friends
```
- **Description**: Get the friends (intersection of followers and following) of the authorized user. 
- **Requires authorization**: Yes.
- **Output**: Basic information of the friends of the authorized user.
- **Schedulable**: Yes (minimum delay: 3600s).

#### Examples

```
GET /twitter/friends
```
```json
[
  { 
    "screen_name": "snoopdogg",
    "url": "http://twitter.com/snoopdogg",
    "id": 1574083,
    "profile_image_url": "https://twitter.com/snoop.jpg",
    "full_name": "Snoop Dogg",
    "followers_count": 822,
    "friends_count": 420
  }
]
```
