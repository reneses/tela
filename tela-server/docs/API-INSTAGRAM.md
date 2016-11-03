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

#### Obtaining an Instagram Module Token

In order to retrieve data from the official Instagram API, an Instagram access token (an Instagram module token on Tela) is required. 

This is responsibility  of each client app, so that they are able to implement and integrate the authorization process as they want. You can read more about that in the official [Instagram documentation](https://www.instagram.com/developer/), particularly in the [client management](https://www.instagram.com/developer/clients/manage/) and [auth flow](https://www.instagram.com/developer/authentication/) sections.

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
- **Description**: Get the friends (intersection of followers and following) of the authorized user. 
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
    full_name: 'Snoop Dogg'
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

# Media Actions

Actions related with information retrieval of media, comments and likes.

### Self Media

```
GET /instagram/self-media
```
- **Description**: Get the latest media of the authorized user. 
- **Requires authorization**: Yes.
- **Output**: Latest media of the authorized user.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `public_content`.

#### Parameters
Name     | Type    | Description                             | Required
:-------:|:-------:|:---------------------------------------:|:---------:
limit    | int     | Number of media to retrieve             | No

#### Examples

```
GET /instagram/self-media?limit=1
```
```json
[
  {
    "id": "1358943_477",
    "caption": {
      "id": 1786524624,
      "text": "What a night!",
      "created_time": 1477840385,
      "from": {
        "username": "snoopdogg",
        "id": 1574083,
        "profile_picture": "https://scontent.cdninstagram.com/snoop.jpg",
        "full_name": "Snoop Dogg",      
      }
    },
    "link": "https://www.instagram.com/p/BMMRE9dDGvf/",
    "type": "image",
    "filter": "Rise",
    "user": {
      "username": "snoopdogg",
      "id": 1574083,
      "profile_picture": "https://scontent.cdninstagram.com/snoop.jpg",
      "full_name": "Snoop Dogg",     
    },
    "tags": [
      "instapic"
    ],
    "location": null,
    "images": {
      "thumbnail": {
        "url": "https://scontent.cdninstagram.com/s150x150/1.jpg",
        "size": "thumbnail",
        "width": 150,
        "height": 150
      },
      "low_resolution": {
        "url": "https://scontent.cdninstagram.com/s320x320/1.jpg",
        "size": "low_resolution",
        "width": 320,
        "height": 320
      },
      "standard_resolution": {
        "url": "https://scontent.cdninstagram.com/s640x640/1.jpg",
        "size": "standard_resolution",
        "width": 640,
        "height": 640
      }
    },
    "videos": {},
    "comments": {
      "data": [],
      "count": 0
    },
    "likes": {
      "data": [],
      "count": 87
    },
    "created_time": 1477840385,
    "users_in_photo": [
      {
        "position": {
          "x": 0.2995169082125604,
          "y": 0.2526978142827535
        },
        "user": {
          "username": "Pedro",
          "id": 389893,
          "profile_picture": "https://pedro.jpg",
          "full_name": "Dj Pedro"
        }
      },
      {
        "position": {
          "x": 0.7141706678602431,
          "y": 0.2455035971223022
        },
        "user": {
          "username": "borja",
          "id": 48333393,
          "profile_picture": "https://borja.jpg",
          "full_name": "Borja"
        }
      }
    ],
    "user_has_liked": false,
    "is_video": false,
    "is_image": true
  }
]
```

### Likes

```
GET /instagram/likes
```
- **Description**: Get the likes of a media. 
- **Requires authorization**: Yes.
- **Output**: Users that have liked the media.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `public_content`.

#### Parameters
Name     | Type    | Description                   | Required
:-------:|:-------:|:-----------------------------:|:---------:
mediaId  | String  | ID of the media               | Yes

#### Examples

```
GET /instagram/likes&mediaId=3948SD
```
```json
[
  {
    "username": "snoopdogg",
    "id": 1574083,
    "profile_picture": "https://scontent.cdninstagram.com/snoop.jpg",
    "full_name": "Snoop Dogg",      
  },   
  {
    "username": "Pedro",
    "id": 389893,
    "profile_picture": "https://pedro.jpg",
    "full_name": "Dj Pedro"
  }
]
```

### Comments

```
GET /instagram/comments
```
- **Description**: Get the comments of a media. 
- **Requires authorization**: Yes.
- **Output**: Comments of the media.
- **Schedulable**: Yes (minimum delay: 3600s).
- **Requires Instagram Scope**: `public_content`.

#### Parameters
Name     | Type    | Description                   | Required
:-------:|:-------:|:-----------------------------:|:---------:
mediaId  | String  | ID of the media               | Yes

#### Examples

```
GET /instagram/comments&mediaId=3948SD
```
```json
[
  {
    "id": 1786344,
    "text": "Amazing!",
    "created_time": 1477840385,
    "from": {
      "username": "snoopdogg",
      "id": 1574083,
      "profile_picture": "https://scontent.cdninstagram.com/snoop.jpg",
      "full_name": "Snoop Dogg",   
    }     
  },
  {
    "id": 1786241,
    "text": "Amazing!",
    "created_time": 1477840385,
    "from": {
      "username": "Pedro",
      "id": 389893,
      "profile_picture": "https://pedro.jpg",
      "full_name": "Dj Pedro"
    } 
  }
]
```