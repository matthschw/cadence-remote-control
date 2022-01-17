/*=============================================================================*
*
* EDcdsRemoteControl
*
* This file provides Cadence SKILL functions that are used for creating
* a remote-control for Cadence-Tools in Java.
*
* Authors: 
* - Matthias Schweikardt
*
* Language: Cadence SKILL
*
* Lint: IQ score is 100 (best is 100)
*       {sklint ?file "EDcdsRemoteControl.il"}
*
* Globals:
* - Symbols:
*    o EDcdsRC
* - Functions:
*    o EDcdsRCfmtCmd
*    o EDcdsRCbuildXML
*    o EDcdsRCescapeXML
*
* Copyright (C) 2021, Electronics & Drives 
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


;SETUP
; Create global symbol EDcdsRC
(putpropq
  (quote EDcdsRC)
  (makeTable "session" nil)
  session
);putpropq

(setarray
  EDcdsRC.session
  "main"
  (list nil)
);setarray

(putpropq 
  (arrayref EDcdsRC.session "main")
  (makeTable "retVals" nil)
  retVals
);putpropq

(putpropq
  (arrayref EDcdsRC.session "main")
  0
  maxIndex
);putpropq


;FUNCTION
; EDcdsRCfmtCmd
;
; Format the return value of a SKILL-Function to an XML-Representation
;
;Parameters:
; result            return value of a SKILL function 
;
; ?returnType       string, either "print" or "string". When "print" is 
;                     provided, the XML is printed to the XIW, when "string"
;                     is provided, the string is returned
;                     
; ?session          identifier of the session
;
;Return:
; t/string       XML-Reference
;
(defun EDcdsRCfmtCmd (result @key (returnType "print") (session "main"))

  (let (retval (maxLength 4000) retstring port file)

    (setq retval (list nil))

    (if result then

      (putpropq retval t valid)
      (putpropq retval (car result) data)

      (if (equal returnType "print") then
        (setq retstring (EDcdsRCbuildXML retval ?session session))
      else
        (setq retstring (EDcdsRCbuildXML retval ?session session ?bCiw nil))
      );if

      (when (geqp (strlen retstring) maxLength)
      
        (setq retstring (EDcdsRCbuildXML retval ?session session ?bCiw nil))

        (setq file (makeTempFileName "/tmp/ed_cds_rc"))
        (setq port (outfile file))
        (fprintf port "%s" retstring)
        (close port)

        (putpropq retval nil  data)
        (putpropq retval file file)

        (if (equal returnType "print") then
          (setq retstring (EDcdsRCbuildXML retval ?session session))
        else
          (setq retstring (EDcdsRCbuildXML retval ?session session ?bCiw nil))
        );if
      );when

    else
      (putpropq retval nil valid)
      (putpropq retval errset.errset error)

      (if (equal returnType "print") then
        (setq retstring (EDcdsRCbuildXML retval ?session session))
      else
        (setq retstring (EDcdsRCbuildXML retval ?session session ?bCiw nil))
      );if
    );if

    (if (equal returnType "print") then
      (printf retstring)
    else
      retstring
    );if
  );let
);defun

;FUNCTION
; EDcdsRCexCmdFile
;
; Evaluate a SKILL-Function that is stored in a file and return a
;  XML-Representation
; 
;
;Parameters:
; file              file path where the SKILL command is stored
;
; ?returnType       string, either "print" or "string". When "print" is 
;                     provided, the XML is printed to the XIW, when "string"
;                     is provided, the string is returned
;                     
; ?session          identifier of the session
;
;Return:
; t/string       XML-Reference
;
(defun EDcdsRCexCmdFile (file @key (returnType "print") (session "main"))

  (let (port command)

    (setq port (infile file))
    (setq command (read port))
    (close port)

    (EDcdsRCfmtCmd
      (errset (eval command))
      ?returnType returnType
      ?session session
    );EDcdsRCfmtCmd
  );let
);defun


;FUNCTION
; EDcdsRCbuildXML
;
; Build the XML-Representation of a SKILL-Dataobject
;
;Parameters:
; content           SKILL-Dataobject
;                     
; ?session          identifier of the session
;
;Return:
; string            XML
;
(defun EDcdsRCbuildXML (content @key (session "main") (bCiw "t"))

  (let (fTraverseDataStructure lBuildList (sRoot "root") listAccu vec)

    (setq lBuildList (tconc nil nil))
    
    (when bCiw
      (tconc lBuildList "<<1")
    );when
   
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

            (
              (and 
                (listp val)
                (plusp (length val))
                (null (car val))
                (apply (quote and) val->?)
              )

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
            
            ((and (isCallable (quote complexp)) (complexp val))

              (tconc
                lBuildList
                (lsprintf
                  "%s<%s type=\"doublecomplex\">\n"
                  sIndentation
                  (EDcdsRCescapeXML sRoot)
                );lsprintf
              );tconc

              (funcall
                fTraverseDataStructure
                (real val)
                "real"
                (strcat sIndentation "\t")
              );funcall

              (funcall
                fTraverseDataStructure
                (imag val)
                "imag"
                (strcat sIndentation "\t")
              );funcall

              (tconc
                lBuildList
                (lsprintf 
                  "%s</%s>\n"
                  sIndentation
                  (EDcdsRCescapeXML sRoot)
                );lsprintf
              );tconc
            )
              
   
            ((and (isCallable (quote drIsWaveform)) (drIsWaveform val))
            
              (tconc
                lBuildList
                (lsprintf
                  "%s<%s type=\"wave\">\n"
                  sIndentation
                  (EDcdsRCescapeXML sRoot)
                );lsprintf
              );tconc

              (setq listAccu (tconc nil nil))
              (setq vec (drGetWaveformXVec val))

              (for i 0 (sub1 (drVectorLength vec))
                (tconc listAccu (drGetElem vec i))
              );for

              (funcall
                fTraverseDataStructure
                (cdar listAccu)
                "x"
                (strcat sIndentation "\t")
              );funcall

              (setq listAccu (tconc nil nil))
              (setq vec (drGetWaveformYVec val))

              (for i 0 (sub1 (drVectorLength vec))
                (tconc listAccu (drGetElem vec i))
              );for

              (funcall
                fTraverseDataStructure
                (cdar listAccu)
                "y"
                (strcat sIndentation "\t")
              );funcall

              (tconc
                lBuildList
                (lsprintf
                  "%s</%s>\n"
                  sIndentation
                  (EDcdsRCescapeXML sRoot)
                );lsprintf
              );tconc
            )

            /*
            ((and (isCallable (quote famIsFamily)) (famIsFamily val))
              
              (tconc
                lBuildList
                (lsprintf
                  "%s<%s type=\"fam\">\n"
                  sIndentation
                  (EDcdsRCescapeXML sRoot)
                );lsprintf
              );tconc

              (foreach sweepValue (famGetSweepValues val) 
                (funcall
                  fTraverseDataStructure
                  (list sweepValue (famValue val sweepValue))
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
            */
            
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
            
              (foreach
                id 
                (getq (arrayref EDcdsRC.session session) retVals)->?

                (when
                  (equal 
                    (arrayref 
                      (getq (arrayref EDcdsRC.session session) retVals)
                      id
                    );arrayref
                    val
                  );equal
                  (setq idx id)
                );when
              );foreach

              (when (equal idx -1)

                (setq idx  (getq (arrayref EDcdsRC.session session) maxIndex))
                (setarray
                  (getq (arrayref EDcdsRC.session session) retVals)
                  (getq (arrayref EDcdsRC.session session) maxIndex)
                  val
                );setarray

                (putpropq
                  (arrayref EDcdsRC.session session)
                  (plus (getq (arrayref EDcdsRC.session session) maxIndex) 1)
                  maxIndex
                );putpropq
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
    
  (when bCiw
    (tconc  lBuildList "2>>")
  );when

  (apply 'strcat (cdar lBuildList))

  ;(cdar lBuildList)
  );let
);defun


;FUNCTION
; EDcdsRCescapeXML
;
; Escape a string s.t. it matches the XML-Syntax
;
;Parameters:
; sSequence         String
;
;Return:
; string            escaped string
;
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
