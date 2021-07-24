(setPrompts
  "[ED-CDS-RC]"
  "[ED-CDS-RC]"
);setPrompts

(putpropq (quote EDcdsRC) (makeTable "returnValues" nil) returnValues)
(putpropq (quote EDcdsRC) 0 maxIndex)

(defun EDcdsRCfmtCmd (result)

  (let (retval data (maxLength 4000) retstring port file)

    (setq retval (list nil))

    (if result then

      (putpropq retval t valid)
      (putpropq retval (car result) data)

      (setq retstring (EDcdsRCbuildXML retval))

      (when (geqp (strlen retstring) maxLength)

        (setq file (makeTempFileName "/tmp/ed_cds_rc"))
        (setq port (outfile file))
        (fprintf port "%s" data)
        (close port)

        (putpropq retval nil data)

        (setq retstring (EDcdsRCbuildXML retval))
      );when

    else
      (putpropq retval nil valid)
      (putpropq retval errset.errset error)
      (setq retstring (EDcdsRCbuildXML retval))
    );if

    (printf retstring)
  );let
);defun


(defun EDcdsRCbuildXML (content)

  (let (fTraverseDataStructure lBuildList (sRoot "root"))
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
                  "%s<%s type=\"fixnum\">%L</%s>\n"
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
                  "%s<%s type=\"flonum\">%L</%s>\n"
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
                  "%s<%s type=\"string\">%s</%s>\n"
                  sIndentation
                  sRoot
                  (EDcdsRCescapeXML val)
                  sRoot
                );lsprintf
              );tconc
            )

            ((dplp val)

              (tconc
                lBuildList
                (lsprintf
                  "%s<%s type=\"dpl\">\n"
                  sIndentation
                  (EDcdsRCescapeXML sRoot)
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
                  (EDcdsRCescapeXML sRoot)
                );lsprintf
              );tconc
            )

            ((listp val)

              (tconc
                lBuildList
                (lsprintf
                  "%s<%s type=\"list\">\n"
                  sIndentation
                  (EDcdsRCescapeXML sRoot)
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
                  (EDcdsRCescapeXML sRoot)
                );lsprintf
              );tconc
            )

            ((symbolp val) 

              (tconc
                lBuildList
                (lsprintf 
                  "%s<%s type=\"symbol\">%s</%s>\n"
                  sIndentation
                  sRoot
                  (EDcdsRCescapeXML (get_pname val))
                  sRoot
                );lsprintf
              );tconc
            )

            (t 

              (setq idx -1)
            
              (foreach id EDcdsRC.returnValues->?
                (when
                  (equal 
                    (arrayref EDcdsRC.returnValues id)
                    val
                  );equal
                  (setq idx id)
                );when
              );foreach

              (when (equal idx -1)
                (setq idx EDcdsRC.maxIndex)
                (setarray
                  EDcdsRC.returnValues
                  EDcdsRC.maxIndex
                  val
                );setarray
                EDcdsRC.maxIndex = EDcdsRC.maxIndex +1
              );when
            
            
              (tconc
                lBuildList
                (lsprintf
                  "%s<%s type=\"complex\">%L</%s>\n"
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
      content 
      (if (or (stringp sRoot) (symbolp sRoot)) sRoot "root")
      ""
    );funcall

    (apply 'strcat (cdar lBuildList))
  );let
);defun


(defun EDcdsRCescapeXML (sSequence)
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
