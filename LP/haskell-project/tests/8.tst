let seq = (Seq [(Empty ("X")), (Push ("X") (Const 3)), (Push ("X") (Const 3)), (Pop ("X") ("Y")), (Print ("Y"))])

let symt = (SymTable [])
let ent = [4, 5, 2, 4, 8, 2]

interpretCommand symt ent seq
