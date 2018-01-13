![Technogram: A new social network for IT specialists](https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/Art.png)

TechnoGram is a new social network for IT specialists. This repo provides Android part of the service.

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
    - [Live DEMO](#live-demo)
- [Credits](#credits)
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

• You can authorize to TechnoGram using your username (or e-mail) and password

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/1.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/2.png" width="240">

### Publications

• You can see publications of other users

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/13.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/14.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/15.png" width="240">

### New Publication

• You can create a new publication by pressing `NEW PUB` at the main screen.

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/3.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/4.png" width="240">

• You can also attach `photos`, `@mentions`, `#hashtags` and `links` (like https://github.com/jookovjook/technogram-andoid) to your publication

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/5.png" width="240">

### Comment posts

• You can also comment posts of other users

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/6.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/7.png" width="240">

### Edit profile

• You an edit your profile's info

<img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/11.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/12.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/8.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/9.png" width="240">     <img src="https://raw.githubusercontent.com/jookovjook/technogram-andoid/master/screenshots/10.png" width="240">

### Live DEMO

• Watch this YouTube live DEMO:

[![TechoGram [DEMO]](https://img.youtube.com/vi/ONhNDH2S7Ns/0.jpg)](https://www.youtube.com/watch?v=ONhNDH2S7Ns)

## Credits

Created by [jookovjook](https://github.com/jookovjook).

[vk.com/jookovjook](https://vk.com/jookovjook)

[fb.com/jookovjook](https://fb.com/jookovjook)

[t.me/jookovjook](https://t.me/jookovjook)
    
You are welcome to participate the project!

## Donations

I'll be gratefull if you donate some funds to my `Etherium wallet`:

```
0x9B9a7B954E4c634b200Be98aa602b7ee9006b05B
```

## License

TechnoGram is released under the Apache 2.0 license.

    Copyright 2017 JookovJook
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
