# cadence-remote-control
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)  

Remote Control for Cadence-Tools in Java

This toolbox provides remote-control capabilities for Cadence-based
tools in Java.

This software was developed for [OpenJDK 1.8](https://openjdk.java.net) 
and [Apache Maven 3.6.3](https://maven.apache.org).
The documentation can be created with 
```bash
mvn javadoc:javadoc
```
and accessed at  `./target/apidocs/index.html`.

## Installation

```bash
mvn install
```

## Known Issues

When the unit tests throw errors, please try first
```bash
mvn install -Dmaven.test.skip=true
```
before executing
```bash
mvn install
```

## License

Copyright (C) 2022, [Reutlingen University](https://www.reutlingen-university.de), [Electronics & Drives](https://www.electronics-and-drives.de/)

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