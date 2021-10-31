(let 
  (
    (skillDir "./src/main/skill") (cxtDir   "./src/main/resources/cxt")
    postfix cid context
  )
  
  (setq
    cid
    (ipcBeginProcess "mvn help:evaluate -Dexpression=context -q -DforceStdout")
  );setq

  (setq context (ipcReadProcess cid 10))

  (when (isDir cxtDir)
    (system (strcat "rm -rf " cxtDir))
  );when
  
  (createDir cxtDir)

  (setContext context)

  (foreach file (getDirFiles skillDir)

    (setq postfix (car (last (parseString file "."))))

    (when (or (equal postfix "il") (equal postfix "ils"))
      (load (strcat skillDir "/" file))
    );when
  );foreach

  (saveContext (strcat cxtDir "/" context  ".cxt"))
);let