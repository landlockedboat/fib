;;; Computation of the factorial of a number

(deffacts factorial
  (fact 1 1)
  (hasta 6)
)

(defrule factorial
   (hasta ?l)
   ?f <- (fact ?x&:(< ?x ?l) ?y)
=>
 (retract ?f)
 (assert (fact (+ ?x 1) (* ?y (+ ?x 1))))
)

