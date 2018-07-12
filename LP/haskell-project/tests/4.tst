let seq = (Seq [(Input ("X")), (Loop (Gt (Var "X") (Const 3)) (Seq [(Assign ("X") (Minus (Var "X") (Const 1))), (Print ("X"))]))])

let symt = (SymTable [])
let ent = [40]

interpretCommand symt ent seq
