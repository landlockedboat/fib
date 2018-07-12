;;; Maximum of a set of numbers
;;; The declarative way

(deffacts maximo
  (val 12)
  (val 33)
  (val 42)
  (val 56)
  (val 64)
  (val 72)
)

(defrule calcula-max
 (val ?x)
 (forall (val ?y) (test  (>= ?x ?y)))
=>
 (printout t ?x crlf)
)
