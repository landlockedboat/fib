;;; Maximum of a set of numbers
;;; The procedural way

(deffacts maximo
  (val 12)
  (val 33)
  (val 42)
  (val 56)
  (val 64)
  (val 72)
)

(defrule init "Inicializa el calculo del maximo"
  (not (max ?))
 =>
 (assert (max 0))
)

(defrule calcula-max
  (val ?x)
  ?h <- (max ?y&:(> ?x ?y))
=>
 (retract ?h)
 (assert (max ?x))
)

(defrule print-max
 (declare (salience -10))
 (max ?x)
=>
 (printout t ?x crlf)
)
