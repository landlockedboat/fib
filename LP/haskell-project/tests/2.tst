let seq = (Seq [(Input ("Z")), (Cond (AND (Gt (Var "Z") (Const 0)) (Gt (Const 30) (Var "Z"))) (Seq [(Assign ("M") (Const 2))]) (Seq [(Assign ("V") (Const 0))]))])

let symt = (SymTable [])
let ent = [40]

interpretCommand symt ent seq
