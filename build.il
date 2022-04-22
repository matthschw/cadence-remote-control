(let 
  (
    (skillDir "./src/main/skill") 
    (cxtResourceDir     "./src/main/resources/cxt")
    (skillResourceDir   "./src/main/resources/skill")
    postfix cid context
  )
  
  (setq
    cid
    (ipcBeginProcess "mvn help:evaluate -Dexpression=context -q -DforceStdout")
  );setq

  (setq context (ipcReadProcess cid 10))

  (when (isDir cxtResourceDir)
    (system (strcat "rm -rf " cxtResourceDir))
  );when
  
  (when (isDir skillResourceDir)
    (system (strcat "rm -rf " skillResourceDir))
  );when
  
  (createDir cxtResourceDir)
  (createDir skillResourceDir)
  
  (setContext context)

  (foreach file (getDirFiles skillDir)

    (setq postfix (car (last (parseString file "."))))

    (when (or (equal postfix "il") (equal postfix "ils"))
      (load (strcat skillDir "/" file))
      (compress (strcat skillDir "/" file) (strcat skillResourceDir "/" file))
    );when
  );foreach

  (saveContext (strcat cxtResourceDir "/" context  ".cxt"))
);let