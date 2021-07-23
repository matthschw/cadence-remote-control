(setPrompts
  "[ED-CDS-RC]"
  "[ED-CDS-RC]"
);setPrompts

(putpropq (quote EDcdsRemoteControl) (makeTable "returnValues" nil) returnValues)
(putpropq (quote EDcdsRemoteControl) 0 maxIndex)

(defun EDcdsRemoteControlEvalCommand (lCommand)

  (let (fTraverseDataStructure lBuildList returnValue (sRoot "root"))

    ;(setq returnValue (errset lCommand))

    (setq lBuildList (tconc nil nil))

    (tconc 
      lBuildList 
      "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
    );tconc

    ;fTraverseDataStructure
    (setq
      fTraverseDataStructure
      (lambda (val sRoot sIndentation)

        (let (idx)
          (cond
            ((fixp val) 
              (tconc 
                lBuildList 
                (lsprintf
                  "%s<%s isNative=\"yes\" type=\"fixnum\">%L</%s>\n"
                  sIndentation
                  sRoot
                  val
                  sRoot
                );lsprintf
              );tconc
            )
            ((floatp val)

              (tconc
                lBuildList
                (lsprintf
                  "%s<%s isNative=\"yes\" type=\"flonum\">%L</%s>\n"
                  sIndentation
                  sRoot
                  val
                  sRoot
                );lsprintf
              );tconc
            )

            ((stringp val) 

              (tconc
                lBuildList
                (lsprintf 
                  "%s<%s isNative=\"yes\" type=\"string\">%s</%s>\n"
                  sIndentation
                  sRoot
                  (EDcdsRemoteControlEscapeXML val)
                  sRoot
                );lsprintf
              );tconc
            )

            ((dplp val)

              (tconc
                lBuildList
                (lsprintf
                  "%s<%s isNative=\"yes\" type=\"dpl\">\n"
                  sIndentation
                  (EDcdsRemoteControlEscapeXML sRoot)
                );lsprintf
              );tconc

              (foreach item val->?
                (funcall
                  fTraverseDataStructure
                  (get val item)
                  item
                  (strcat sIndentation "\t")
                );funcall
              );foreach

              (tconc
                lBuildList
                (lsprintf 
                  "%s</%s>\n"
                  sIndentation
                  (EDcdsRemoteControlEscapeXML sRoot)
                );lsprintf
              );tconc
            )

            ((listp val)

              (tconc
                lBuildList
                (lsprintf
                  "%s<%s isNative=\"yes\" type=\"list\">\n"
                  sIndentation
                  (EDcdsRemoteControlEscapeXML sRoot)
                );lsprintf
              );tconc
                  
              (foreach item val
                (funcall
                  fTraverseDataStructure
                  item
                  "entry"
                  (strcat sIndentation "\t")
                );funcall
              );foreach
                  
              (tconc
                lBuildList
                (lsprintf
                  "%s</%s>\n"
                  sIndentation
                  (EDcdsRemoteControlEscapeXML sRoot)
                );lsprintf
              );tconc
            )
            (t 

              (setq idx -1)
            
              (foreach id EDcdsRemoteControl.returnValues->?
                (when
                  (equal 
                    (arrayref EDcdsRemoteControl.returnValues id)
                    val
                  );equal
                  (setq idx id)
                );when
              );foreach

              (when (equal idx -1)
                (setq idx EDcdsRemoteControl.maxIndex)
                (setarray
                  EDcdsRemoteControl.returnValues
                  EDcdsRemoteControl.maxIndex
                  val
                );setarray
                EDcdsRemoteControl.maxIndex = EDcdsRemoteControl.maxIndex +1
              );when
            
            
              (tconc
                lBuildList
                (lsprintf
                  "%s<%s>%L<%s>\n"
                  sIndentation
                  sRoot
                  idx
                  sRoot
                );lsprintf
              );tconc
            )
          );cond
        );let
      );lambda
    );setq

    (funcall
      fTraverseDataStructure 
      lCommand 
      (if (or (stringp sRoot) (symbolp sRoot)) sRoot "root")
      ""
    );funcall

    (apply 'strcat (cdar lBuildList))
  );let
);defun

(defun EDcdsRemoteControlEscapeXML (sSequence)
  (buildString
    (foreach mapcar x (parseString sSequence "")
      (cond
        ((equal x "\"") "&quot;")
        ((equal x "'")  "&apos;")
        ((equal x "<") "&lt;")
        ((equal x ">")  "&gt;")
        ((equal x "&")  "&amp;")            
        (t x)
      );cond
    );foreach
    ""
  );buildString
);defun