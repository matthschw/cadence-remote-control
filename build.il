/*=============================================================================*
*
* Building the SKILL backend for 'cadence-remote-control'
*  The SKILL code of this remote control is compiled to a context file and 
*  compressed Skill file
*
* Copyright (C) 2022, Electronics & Drives 
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program. If not, see 
* [https://www.gnu.org/licenses/](https://www.gnu.org/licenses).
*
*=============================================================================*/
(let 
  (
    ; Skill source files directory
      (skillDir "./src/main/skill") 
    ; Skill context files directory
      (cxtResourceDir "./src/main/resources/cxt")
    ; Skill files (compressed) directory
      (skillResourceDir "./src/main/resources/skill")

    postfix cid context
  )
  
  ;get name from pom.xml
  (setq
    cid
    (ipcBeginProcess "mvn help:evaluate -Dexpression=context -q -DforceStdout")
  );setq

  (setq context (ipcReadProcess cid 10))

  ;clear context file directory
  (when (isDir cxtResourceDir)
    (system (strcat "rm -rf " cxtResourceDir))
  );when
  
  ;clear skill file directory
  (when (isDir skillResourceDir)
    (system (strcat "rm -rf " skillResourceDir))
  );when
  
  ;create result directories
  (createDir cxtResourceDir)
  (createDir skillResourceDir)
  
  ;init context
  (setContext context)

  ;iterate over all files in Skill directory
  (foreach file (getDirFiles skillDir)

    (setq postfix (car (last (parseString file "."))))

    ;load file when valid Skill file
    (when (or (equal postfix "il") (equal postfix "ils"))
      (load (strcat skillDir "/" file))
      (compress (strcat skillDir "/" file) (strcat skillResourceDir "/" file))
    );when
  );foreach

  (saveContext (strcat cxtResourceDir "/" context  ".cxt"))
);let