![Technogram: A new social network for IT specialists](https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/Art.png)

TechnoGram is a new social network for IT specialists. This repo provides Android part of network.

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Communication](#communication)
- [Installation](#installation)
- [Overview](#overview)
    - [Authorization](#authorization)
    - [Publications](#publications)
    - [New publication](#new-publication)
    - [Comment posts](#comment-posts)
    - [Edit profile](#edit-profile)
- [FAQ](#faq)
- [Contacts](#ontacts)
- [Donations](#donations)
- [License](#license)

## Introduction

TechnoGram is a social network for IT specialists. There is a place, where it's users can:
• Share ther ideas and achievements to other people
• Discuss topics with
• Share their goals

Current repo represent TechnoGram mobile app for Android.

## Features

- [x] Username/e-mail authorization
- [x] Create posts with attached images, description, @mentions, #hashtags, links
- [x] See posts of other users
- [x] Leave comments to posts
- [x] Like, double-like on posts
- [x] Edit own profile (username, name, surname, email, bio)
- [x] See profiles of other users

## Requirements

- Android 5.0+ device
- LAMP server

## Communication

- If you **need help**, use [Stack Overflow](http://stackoverflow.com/questions/tagged/technogram). (Tag 'technogram')
- If you'd like to **ask a general question**, use [Stack Overflow](http://stackoverflow.com/questions/tagged/technogram).
- If you **found a bug**, open an issue.
- If you **have a feature request**, open an issue.
- If you **want to contribute**, submit a pull request.

## Installation

### LAMP

Firstly clone [technogram-server](https://github.com/jookovjook/technogram-server) to your LAMP server.

```bash
$ git clone https://github.com/jookovjook/technogram-server
```

Use the installation guide at [technogram-server](https://github.com/jookovjook/technogram-server) repository.

### Android Studio

Clone [current](https://github.com/jookovjook/technogram-android) respository 

```bash
$ git clone https://github.com/jookovjook/technogram-android
```

Add your server's addres to `utils/Config.java`

```Java
public static final String SERVER_URL = "http://your.server.com/";
```

Build project and run it on your Android device.

## Overview

### Authorization

You can authorize to TechnoGram using your username (or e-mail) and password

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/1.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/2.png" width="240">

### Publications

You can see publications of other users

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/13.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/14.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/15.png" width="240">

### New Publication

You can create a new publication by pressing `NEW PUB` at the main screen.

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/3.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/4.png" width="240">

You can also attach `photos`, `@mentions`, `#hashtags` and `links` (like https://github.com/jookovjook/technogram-andoid) to your publication

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/5.png" width="240">

### Comment posts

You can also comment posts of other users

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/6.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/7.png" width="240">

### Edit profile

You an edit your profile's info

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/11.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/12.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/8.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/9.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/10.png" width="240">

## FAQ

### What's the origin of the TechnoGram?

Alamofire is named after the [Alamo Fire flower](https://aggie-horticulture.tamu.edu/wildseed/alamofire.html), a hybrid variant of the Bluebonnet, the official state flower of Texas.

### What logic belongs in a Router vs. a Request Adapter?

Simple, static data such as paths, parameters and common headers belong in the `Router`. Dynamic data such as an `Authorization` header whose value can changed based on an authentication system belongs in a `RequestAdapter`.

The reason the dynamic data MUST be placed into the `RequestAdapter` is to support retry operations. When a `Request` is retried, the original request is not rebuilt meaning the `Router` will not be called again. The `RequestAdapter` is called again allowing the dynamic data to be updated on the original request before retrying the `Request`.

## Credits

Alamofire is owned and maintained by the [Alamofire Software Foundation](http://alamofire.org). You can follow them on Twitter at [@AlamofireSF](https://twitter.com/AlamofireSF) for project updates and releases.

### Security Disclosure

If you believe you have identified a security vulnerability with Alamofire, you should report it as soon as possible via email to security@alamofire.org. Please do not post it to a public issue tracker.

## Donations

The [ASF](https://github.com/Alamofire/Foundation#members) is looking to raise money to officially register as a federal non-profit organization. Registering will allow us members to gain some legal protections and also allow us to put donations to use, tax free. Donating to the ASF will enable us to:

- Pay our legal fees to register as a federal non-profit organization
- Pay our yearly legal fees to keep the non-profit in good status
- Pay for our mail servers to help us stay on top of all questions and security issues
- Potentially fund test servers to make it easier for us to test the edge cases
- Potentially fund developers to work on one of our projects full-time

The community adoption of the ASF libraries has been amazing. We are greatly humbled by your enthusiasm around the projects, and want to continue to do everything we can to move the needle forward. With your continued support, the ASF will be able to improve its reach and also provide better legal safety for the core members. If you use any of our libraries for work, see if your employers would be interested in donating. Our initial goal is to raise $1000 to get all our legal ducks in a row and kickstart this campaign. Any amount you can donate today to help us reach our goal would be greatly appreciated.

<a href='https://pledgie.com/campaigns/31474'><img alt='Click here to lend your support to: Alamofire Software Foundation and make a donation at pledgie.com !' src='https://pledgie.com/campaigns/31474.png?skin_name=chrome' border='0' ></a>

## License

Alamofire is released under the MIT license. [See LICENSE](https://github.com/Alamofire/Alamofire/blob/master/LICENSE) for details.
