let seq = (Seq [(Input ("Z")), (Print ("Z"))])

let symt = (SymTable [])
let ent = [40]

interpretCommand symt ent seq
