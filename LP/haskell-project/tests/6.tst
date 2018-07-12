let seq = (Seq [(Input ("X")), (Input ("Y")), (Cond (OR (OR (Gt (Var "X") (Const 0)) (Eq (Var "X") (Const 0))) (NOT (Gt (Const 0) (Var "Y")))) (Seq [(Assign ("Z") (Const 2)), (Loop (Gt (Var "X") (Var "Y")) (Seq [(Assign ("X") (Minus (Var "X") (Const 1))), (Assign ("Z") (Times (Var "Z") (Var "Z")))]))]) (Seq [(Assign ("Z") (Const 0))])), (Print ("Z"))])

let symt = (SymTable [])
let ent = [3, 1]

interpretCommand symt ent seq
