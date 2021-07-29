# cadence-remote-control

Remote Control for Cadence-Tools in Java

This toolbox provides remote-control capabilities for Cadence-based
tools in Java.

# Installation

Clone this repository:

```bash
$ git clone https://github.com/matthschw/cadence-remote-control.git
```

`cadence-remote-contro` into the directory.

## Setup
Add the dependency to your project

```xml
<dependency>
  <groupId>edlab.eda</groupId>
  <artifactId>cadence.rc</artifactId>
  <version>1.0.1</version>
</dependency>
```

Import the corresponding package to your code
```java
import edlab.eda.reader.cadence.rc.*;
```

# Utilization

The toolbox provides to different use-cases how to remote control 
Cadence-Tools:

## SkillSession
This class is used when the Cadence-Tool is started and controlled from Java.

## SkillChildSession

This class is used when Java is started as Subprocess (IPC) in
Virtuoso.


# TODO

- [ ] Add relevant commands as templates
- [ ] Add *SkillChildSession*

# Derivatives

- [virtuoso-remote-control](https://github.com/matthschw/virtuoso-remote-control)
- [spectre-remote-control](https://github.com/matthschw/spectre-remote-control)

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
