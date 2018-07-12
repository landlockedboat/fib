;;; Sorting of the elements of a vector

(deftemplate pos
  (slot index (type INTEGER))
  (slot value (type INTEGER))
)

(deffacts vector 
  (pos (index 1) (value 15))
  (pos (index 2) (value 7))
  (pos (index 3) (value 4))
  (pos (index 4) (value 2))
)

(defrule ordena-bubble
   ?f1 <- (pos (index ?p1) (value ?v1))
   ?f2 <- (pos (index ?p2&:(= ?p2 (+ ?p1 1)))  (value ?v2&:(< ?v2 ?v1)))
=>
 (modify ?f1 (value ?v2))
 (modify ?f2 (value ?v1))
)

(defrule imprime-res
  (declare (salience -10))
  ?f1 <- (pos (index ?p1) (value ?v))
  (forall (pos (index ?p2)) (test (<= ?p1 ?p2)))
 =>
  (printout t ?v crlf)
  (retract ?f1)
)

