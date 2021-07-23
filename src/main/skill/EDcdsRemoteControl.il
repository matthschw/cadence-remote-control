(setPrompts
  "[ED-CDS_RC]"
  "[ED-CDS_RC]"
);setPrompts

(putpropq (quote EDcdsRemoteControl) (makeTable "returnValues" nil) returnValues)
(putpropq (quote EDcdsRemoteControl) 0 maxIndex)

(defun EDcdsRemoteControlEvalCommand (lCommand)

  (let (fTraverseDataStructure lBuildList)

    (setq lBuildList (tconc nil nil))

    (tconc 
      lBuildList 
      "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
    );tconc

    ;fTraverseDataStructure
    (setq
      fTraverseDataStructure
      (lambda (aVal sRoot sIndentation)

        (cond

          ((fixp aVal) 

            (tconc 
              lBuildList 
              (lsprintf
                "%s<%s isNative=\"yes\" type=\"fixnum\">%L</%s>\n"
                sIndentation sRoot aVal sRoot
              );lsprintf
            );lBuildList
          )

          ((floatp aVal)

            (tconc
              lBuildList
              (lsprintf
                "%s<%s isNative=\"yes\" type=\"flonum\">%L</%s>\n"
                sIndentation
                sRoot
                aVal
                sRoot
              )
            );tconc
          )

          ((stringp aVal) 

            (tconc
              lBuildList
              (lsprintf 
                "%s<%s isNative=\"yes\" type=\"string\">%s</%s>\n"
                sIndentation
                sRoot
                (EDpredictEscapeXML aVal)
                sRoot
              );lsprintf
            );tconc
          )

          ((dplp aVal)

            (tconc
              lBuildList
              (lsprintf
                "%s<%s isNative=\"yes\" type=\"dpl\">\n"
                sIndentation
                (EDcdsRemoteControlEscapeXML  sRoot)
              );lsprintf
            );tconc

            (foreach item aVal->?
              (funcall
                fTraverseDataStructure
                (get aVal item)
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
            	)
            )
          )

          ((symbolp aVal) 

            (tconc
              lBuildList
              (lsprintf
                "%s<%s isNative=\"yes\" type=\"symbol\">%L</%s>\n"
                sIndentation sRoot aVal sRoot
              );lsprintf
            );tconc
          )

          ((listp aVal)

            (tconc
              lBuildList
              (lsprintf
                "%s<%s isNative=\"yes\" type=\"list\">\n"
                sIndentation
                (EDpredictEscapeXML sRoot)
              );lsprintf
            );tconc
                
            (foreach item aVal
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
            	)
            )
          )
          (t 
          
          	(foreach 
          	
          	);
          
          
            (tconc
              lBuildList
              (lsprintf
                "%s<%s isNative=\"yes\" type=\"list\">\n"
                sIndentation
                (EDpredictEscapeXML sRoot)
              );lsprintf
            );tconc
          )
        );cond
      );lambda
    );setq

    (funcall
      fTraverseDataStructure 
      aVal 
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
