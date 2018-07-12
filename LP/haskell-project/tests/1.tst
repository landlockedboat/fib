let seq = (Seq [(Assign ("X") (Const 1)), (Cond (Gt (Var "X") (Const 0)) (Seq [(Assign ("Z") (Const 1))]) (Seq []))])

let symt = (SymTable [])
let ent = []

interpretCommand symt ent seq
