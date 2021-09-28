# cadence-remote-control
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)  

Remote Control for Cadence-Tools in Java

This toolbox provides remote-control capabilities for Cadence-based
tools in Java.

# Installation

Clone this repository:

```bash
$ git clone https://github.com/electronics-and-drives/cadence-remote-control.git
```

`cd cadence-remote-control` into the directory and install it

```bash
mvn install
```

## Setup
Add the dependency to your project

```xml
<dependency>
  <groupId>edlab.eda</groupId>
  <artifactId>cadence.rc</artifactId>
  <version>0.0.1</version>
</dependency>
```

Import the corresponding package to your code
```java
import edlab.eda.cadence.rc.*;
```

# Utilization

The toolbox provides to different use-cases how to remote control 
Cadence-Tools:

## SkillInteractiveSession
This class is used when the Cadence-Tool is started and controlled from Java.

```java
// Create an interactive session
SkillInteractiveSession session = new SkillInteractiveSession();

// Start the session
session.start();

// create a command template for the command "plus"
SkillCommandTemplate template = SkillCommandTemplate.build("plus", 2);

// create the command "(plus 1 41)"
SkillCommand command = template.buildCommand(
    new EvaluableToSkill[] { new SkillFixnum(1), new SkillFixnum(41) });

// evaluate the command in the session
SkillDataobject obj = session.evaluate(command);

// typecast result
SkillFixnum num = (SkillFixnum) obj;

System.out.println(num.getFixnum());

// close session
session.stop();
```

## SkillChildSession


From Cadence tools, a Java-Socket can be started.

```lisp
(load "./src/main/skill/EDcdsRemoteControl.il")
(ipcSkillProcess "java -cp <PATH-TO-JAR> edlab.eda.cadence.rc.CadenceSocket")
```

When the socket is started, the file ´.ed_cds_rc_socket´ is created in the
working directory, which contains the socket number.

Afterwards, the session can be accessed from Java

```java
// Create a socket session for the started port
SkillSocketSession session = new SkillSocketSession(port);

// Start the session
session.start();

// create a command template for the command "plus"
SkillCommandTemplate template = SkillCommandTemplate.build("plus", 2);

// create the command "(plus 1 41)"
SkillCommand command = template.buildCommand(
    new EvaluableToSkill[] { new SkillFixnum(1), new SkillFixnum(41) });

// evaluate the command in the session
SkillDataobject obj = session.evaluate(command);

// typecast result
SkillFixnum num = (SkillFixnum) obj;

System.out.println(num.getFixnum());

// close session
session.stop();
```


## License

Copyright (C) 2021, [Electronics & Drives](https://www.electronics-and-drives.de/)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see 
[https://www.gnu.org/licenses/](https://www.gnu.org/licenses).