let seq = (Seq [(Empty ("X")), (Cond (Gt (Var "X") (Const 3)) (Seq [(Print ("X"))]) (Seq []))])

let symt = (SymTable [])
let ent = [4, 5, 2, 4, 8, 2]

interpretCommand symt ent seq
