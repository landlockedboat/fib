let seq = (Seq [(Input ("X")), (Empty ("P")), (Loop (OR (Gt (Var "X") (Const 0)) (Eq (Var "X") (Const 0))) (Seq [(Input ("Y")), (Push ("P") (Var "Y")), (Assign ("X") (Minus (Var "X") (Const 1)))])), (Assign ("S") (Const 0)), (Size ("P") ("L")), (Loop (Gt (Var "L") (Const 0)) (Seq [(Pop ("P") ("Y")), (Assign ("S") (Plus (Var "S") (Var "Y"))), (Assign ("L") (Minus (Var "L") (Const 1)))])), (Print ("S"))])

let symt = (SymTable [])
let ent = [4, 5, 2, 4, 8, 2]

interpretCommand symt ent seq
