let seq = (Seq [(Empty ("P")), (Assign ("X") (Const 3)), (Loop (Gt (Var "X") (Const 0)) (Seq [(Push ("P") (Var "X")), (Assign ("X") (Minus (Var "X") (Const 1))), (Print ("X"))]))])

let symt = (SymTable [])
let ent = []

interpretCommand symt ent seq
