import System.IO
import System.Random

data Command a =
  Seq [Command a]
  | Assign Ident (NExpr a)
  | Input Ident
  | Print Ident
  | Empty Ident
  | Pop Ident Ident
  | Size Ident Ident
  | Push Ident (NExpr a)
  | Cond (BExpr a) (Command a) (Command a)
  | Loop (BExpr a) (Command a)
  deriving (Read)

type Ident = String

data NExpr a =
  Var Ident
  | Const a
  | Plus (NExpr a) (NExpr a)
  | Minus (NExpr a) (NExpr a)
  | Times (NExpr a) (NExpr a)
  deriving (Read)

data BExpr a =
    AND (BExpr a) (BExpr a)
  | OR (BExpr a) (BExpr a)
  | NOT (BExpr a)
  | Gt (NExpr a) (NExpr a)
  | Eq (NExpr a) (NExpr a)
  deriving (Read)

type Type = String

data Value a =
  Single a
  | Stack [a]
  deriving (Show, Read)

isSt (Stack [a]) = True
isSt _ = False

popSt :: (Value a) -> (Either String (Value a, Value a))
popSt (Stack []) = Left "empty stack"
popSt (Stack (x:xs)) = Right (Single x, Stack xs)

pushSt :: (Value a) -> (Value a) -> (Value a)
pushSt (Stack l) (Single a) = Stack (a:l)

modSt :: (Symbol a) -> (Value a) -> (Symbol a)
modSt (Symbol ident v) newV = Symbol ident newV

isSing (Single a) = True
isSing _ = False

fromSing (Single a) = a

lenSt :: (Num a) => (Value a) -> a
lenSt (Stack []) = 0
lenSt (Stack (x:xs))
  | null xs = 0
  | otherwise = 1 + (lenSt (Stack xs))

data Symbol a = Symbol Ident (Value a) deriving (Show)

getIdent (Symbol i _) = i

getType (Symbol _ (Single a)) = "single"
getType (Symbol _ _) = "stack"

getVal (Symbol _ v) = v

searchVal :: (SymTable a) -> Ident -> (Maybe a)
searchVal symt ident
  | isSym symt ident = Just (fromSing (getVal (getSym symt ident)))
  | otherwise = Nothing

searchType :: (SymTable a) -> Ident -> String
searchType symt ident
  | isSym symt ident = getType (getSym symt ident)
  | otherwise = "single"

isSym :: (SymTable a) -> Ident -> Bool
isSym (SymTable l@(x:xs)) ident
  | getIdent x == ident = True
  | null xs = False
  | otherwise = isSym (SymTable xs) ident
isSym (SymTable []) ident = False

data SymTable a = SymTable [Symbol a] deriving (Show)

getSym :: (SymTable a) -> Ident -> (Symbol a)
getSym (SymTable (x:xs)) ident
  | getIdent x == ident = x
  | otherwise = getSym (SymTable xs) ident

newSym :: (SymTable a) -> (Symbol a) -> (SymTable a)
newSym (SymTable (x:xs)) a = SymTable (a:x:xs)
newSym (SymTable []) a = SymTable [a]

delSym :: (SymTable a) -> Ident -> (SymTable a)
delSym (SymTable l) ident = __delSym [] l ident
  where
    __delSym :: [Symbol a] -> [Symbol a] -> Ident -> (SymTable a)
    __delSym l r@(x:xs) ident
      | null r = SymTable l
      | null xs && getIdent x == ident = SymTable l
      | null xs = SymTable(l ++ [x])
      | getIdent x == ident = SymTable(l ++ xs)
      | otherwise = __delSym (l++[x]) xs ident

popSym :: (SymTable a) -> Ident -> Ident -> (Either String (SymTable a))
popSym (SymTable l) id1 id2 = __popSym [] l id1 id2
  where
    __popSym l r@(x:xs) id1 id2
      | null xs && getIdent x == id1 =
        if err
          then Left (fromLeft xv)
          else Right (updSym (SymTable (l++[modSt x newV])) id2 o)
      | getIdent x == id1 =
        if err
          then Left (fromLeft xv)
          else Right (updSym (SymTable (l++[modSt x newV]++xs)) id2 o)
      | otherwise = __popSym (l++[x]) xs id1 id2
        where
          xv = popSt (getVal x)
          err = isLeft xv
          newV = snd (fromRight xv)
          o = (fst (fromRight xv))

sizeinSym :: (Num a) => (SymTable a) -> Ident -> Ident -> (SymTable a)
sizeinSym (SymTable l) id1 id2 =
  updSym (SymTable l) id2 (Single siz)
  where
    siz = lenSt (getVal (getSym (SymTable l) id1) )

pushSym :: (SymTable a) -> Ident -> (Value a) -> (SymTable a)
pushSym (SymTable l) id1 singv = __pushSym [] l id1 singv
  where
    __pushSym l r@(x:xs) id1 singv
      | null xs && getIdent x == id1 =
        SymTable(l++[modSt x newV])
      | getIdent x == id1 =
        SymTable(l++[modSt x newV]++xs)
      | otherwise = __pushSym (l++[x]) xs id1 singv
        where
          newV = pushSt (getVal x) singv

updSym :: (SymTable a) -> Ident -> (Value a) -> (SymTable a)
updSym symt ident val
  | not (isJust (searchVal symt ident)) =
    newSym symt (Symbol ident val)
  | otherwise =
    newSym (delSym symt ident) (Symbol ident val)

class Evaluable e where
  eval :: (Num a, Ord a) => (Ident -> Maybe a) -> (e a) -> (Either String a)
  typeCheck :: (Ident -> String) -> (e a) -> Bool

getBool :: String -> Bool
getBool "True" = True
getBool a = False

isBool :: String -> Bool
isBool "True" = True
isBool "False" = True
isBool a = False

fromLeft (Left a) = a

fromRight (Right a) = a

isLeft (Left a) = True
isLeft _ = False

isRight (Right a) = True
isRight _ = False

fromJust (Just a) = a

isJust (Just a) = True
isJust _ = False

instance Evaluable (BExpr) where
  eval f (AND a b)
    | isBool ea && isBool eb = Left (show ((getBool ea) && (getBool eb)))
    | isBool ea = Left eb
    | isBool eb = Left ea
    | otherwise = Left ea
    where
      ea = fromLeft (eval f a)
      eb = fromLeft (eval f b)
  eval f (OR a b)
    | isBool ea && isBool eb = Left (show ((getBool ea) || (getBool eb)))
    | isBool ea = Left eb
    | isBool eb = Left ea
    | otherwise = Left ea
    where
      ea = fromLeft (eval f a)
      eb = fromLeft (eval f b)
  eval f (NOT a)
    | isBool ea = Left (show (not (getBool ea)))
    | otherwise = Left ea
    where
      ea = fromLeft (eval f a)
  eval f (Gt a b)
    | (isRight ea) && (isRight eb) = Left $ show ((fromRight ea) > (fromRight eb))
    | isLeft ea = Left (fromLeft ea)
    | isLeft eb = Left (fromLeft eb)
    where
      ea = eval f a
      eb = eval f b
  eval f (Eq a b)
    | (isRight ea) && (isRight eb) = Left $ show ((fromRight ea) == (fromRight eb))
    | isLeft ea = Left (fromLeft ea)
    | isLeft eb = Left (fromLeft eb)
    where
      ea = eval f a
      eb = eval f b
  typeCheck f (AND a b) = (typeCheck f a) && (typeCheck f b)
  typeCheck f (OR a b) = (typeCheck f a) && (typeCheck f b)
  typeCheck f (NOT a) = typeCheck f a
  typeCheck f (Gt a b) = (typeCheck f a) && (typeCheck f b)
  typeCheck f (Eq a b) = (typeCheck f a) && (typeCheck f b)

instance Evaluable(NExpr) where
  eval f (Var a)
    | not (isJust (f a)) = Left "undefined variable"
    | otherwise = Right (fromJust (f a))
  eval f (Const a) = Right a
  eval f (Plus a b)
    | (isRight ea) && (isRight eb) = Right (rea + reb)
    | isLeft ea = Left (fromLeft ea)
    | isLeft eb = Left (fromLeft eb)
    where
      ea = eval f a
      eb = eval f b
      rea = fromRight ea
      reb = fromRight eb
  eval f (Minus a b)
    | (isRight ea) && (isRight eb) = Right (rea - reb)
    | isLeft ea = Left (fromLeft ea)
    | isLeft eb = Left (fromLeft eb)
    where
      ea = eval f a
      eb = eval f b
      rea = fromRight ea
      reb = fromRight eb
  eval f (Times a b)
    | (isRight ea) && (isRight eb) = Right (rea * reb)
    | isLeft ea = Left (fromLeft ea)
    | isLeft eb = Left (fromLeft eb)
    where
      ea = eval f a
      eb = eval f b
      rea = fromRight ea
      reb = fromRight eb
  typeCheck f (Var a) = f a == "single"
  typeCheck f (Const a) = True
  typeCheck f (Plus a b) = (typeCheck f a) && (typeCheck f b)
  typeCheck f (Minus a b) = (typeCheck f a) && (typeCheck f b)
  typeCheck f (Times a b) = (typeCheck f a) && (typeCheck f b)

trl (a, _, _) = a

trm (_, a, _) = a

trr (_, _, a) = a

concatPrint :: ((Either String [b]), x, y) -> [b] -> ((Either String [b]), x, y)
concatPrint ((Left a), x, y) v = ((Left a), x, y)
concatPrint ((Right a), x, y) v = ((Right (v++a)), x, y)

interpretCommand :: (Num a, Ord a) =>
  SymTable a -> [a] -> Command a -> ((Either String [a]),SymTable a, [a])
interpretCommand symt a (Assign ident nex)
  | typeCheck (searchType symt) nex =
    if isRight evalnex
      then (Right [], updSym symt ident (Single (fromRight evalnex)), a)
      else (Left (fromLeft evalnex), symt, a)
  | otherwise = (Left "type error", symt, a)
    where
      evalnex = eval (searchVal symt) nex
interpretCommand symt (x:xs) (Input ident) =
  (Right [],
  updSym symt ident (Single x),
  xs)
interpretCommand symt a (Print ident)
  | isSym symt ident =
    if isSing valSy
      then (Right [fromSing (valSy)], symt, a)
      else (Left "type error", symt, a)
  | otherwise = (Left "undefined variable", symt, a)
  where
    valSy = getVal (getSym symt ident)
interpretCommand symt a (Empty ident) =
  (Right [],
  updSym symt ident (Stack []),
  a)
interpretCommand symt a (Pop id1 id2)
  | not (isSym symt id1) = (Left "undefined variable", symt, a)
  | isRight newSymt = (Right [], (fromRight newSymt), a)
  | otherwise = (Left (fromLeft newSymt), symt, a)
  where
    newSymt = popSym symt id1 id2
interpretCommand symt a (Size id1 id2)
  | not (isSym symt id1) = (Left "undefined variable", symt, a)
  | otherwise = (Right [], sizeinSym symt id1 id2, a)
interpretCommand symt a (Push id1 nex)
  | not (isSym symt id1) = (Left "undefined variable", symt, a)
  | otherwise = (Right [], pushSym symt id1 (Single (fromRight (eval (searchVal symt) nex))), a)
interpretCommand symt ent (Cond a c1 c2)
  | not (typeCheck (searchType symt) a) = (Left "type error", symt, ent)
  | (isBool fea) && (getBool fea) = interpretCommand symt ent c1
  | isBool fea = interpretCommand symt ent c2
  | otherwise = (Left fea, symt, ent)
  where
    fea = fromLeft (eval (searchVal symt) a)
interpretCommand symt ent (Loop a c)
  | isLeft (trl ret) = ret
  | not (typeCheck (searchType symt) a) = (Left "type error", symt, ent)
  | (isBool fea) && (getBool fea) = concatPrint (interpretCommand symt2 ent2 (Loop a c)) prnt
  | isBool fea = (Right [], symt, ent)
  | otherwise = (Left fea, symt, ent)
  where
    fea = fromLeft (eval (searchVal symt) a)
    ret = interpretCommand symt ent c
    prnt = fromRight (trl ret)
    symt2 = trm ret
    ent2 = trr ret
interpretCommand symt ent (Seq (x:xs))
  | isLeft (trl ret) = ret
  | null xs = ((Right prnt), symt2, ent2)
  | otherwise = concatPrint (interpretCommand symt2 ent2 (Seq xs)) prnt
  where
    ret = interpretCommand symt ent x
    prnt = fromRight (trl ret)
    symt2 = trm ret
    ent2 = trr ret


interpretProgram:: (Num a,Ord a) => [a] -> Command a -> (Either String [a])
interpretProgram ent comm = trl ret
  where
    ret = interpretCommand (SymTable []) ent comm

instance Show a => Show(BExpr a) where
  show (AND x y) = (show x) ++ " AND " ++ (show y)
  show (OR x y) = (show x) ++ " OR " ++ (show y)
  show (NOT x) = "NOT " ++ show x
  show (Gt x y) = (show x) ++ " > " ++ (show y)
  show (Eq x y) = (show x) ++ " = " ++ (show y)

instance Show a => Show(NExpr a) where
  show (Var x) = x
  show (Const x) = show x
  show (Plus x y) =
    show x ++ " + " ++ show y
  show (Minus x y) =
    show x ++ " - " ++ show y
  show (Times x y) =
    show x ++ " * " ++ show y

showc :: Show a => Command a -> String -> String
showc (Seq []) s    = ""
showc (Seq (x:xs)) s  =
  (showc x s) ++ "\n" ++ (showc (Seq xs) s)

showc (Cond be c1 (Seq [])) s =
  s ++ "IF " ++ (show be) ++ " THEN\n" ++
  (showc c1 ("  " ++ s)) ++ s ++ "END\n"

showc (Cond be c1 c2) s =
  s ++ "IF " ++ (show be) ++ " THEN\n" ++
  (showc c1 ("  " ++ s)) ++ s ++ "ELSE\n" ++
  (showc c2 ("  " ++ s)) ++ s ++ "END"

showc (Assign x y) s = s ++ x ++ " := " ++ (show y)
showc (Input x) s = s ++ "INPUT " ++ x
showc (Print x) s = s ++ "PRINT " ++ x
showc (Empty x) s = s ++ "EMPTY " ++ x
showc (Pop x y) s = s ++ "POP " ++ x ++ " " ++ y
showc (Size x y) s = s ++ "SIZE " ++ x ++ " " ++ y
showc (Push x y) s = s ++ "PUSH " ++ x ++ " " ++ (show y)
showc (Loop be c1) s =
  s ++ "WHILE " ++ (show be) ++ "\n" ++ s ++ "DO\n" ++
  (showc c1 ("  " ++ s)) ++ s ++ "END"

instance Show a => Show(Command a) where
  show x = showc x ""

printProg ret = do
  if isLeft ret then
      print (fromLeft ret)
    else
      print (fromRight ret)
  return ()

doIt n ent prog
  | n == 0 = do
    return()
  | otherwise = do
    print (take 20 ent)
    printProg $ interpretProgram ent prog
    doIt (n - 1) ent prog
    return()

execInt eMode randInt prog
  | eMode == 0 = do
    entryString <- getLine
    printProg $ interpretProgram ((read entryString :: [Int])++randInt) prog
    return()
  | eMode == 1 = do
    print (take 20 randInt)
    printProg $ interpretProgram randInt prog
    return()
  | otherwise = do
    nTestsString <- getLine
    let nTests = read nTestsString :: Int
    doIt nTests randInt prog
    return()

execDb eMode randDb prog
  | eMode == 0 = do
    entryString <- getLine
    printProg $ interpretProgram ((read entryString :: [Double])++randDb) prog
    return()
  | eMode == 1 = do
    print (take 20 randDb)
    printProg $ interpretProgram randDb prog
    return()
  | otherwise = do
    nTestsString <- getLine
    let nTests = read nTestsString :: Int
    return()

main = do
  handle <- openFile "programhs.txt" ReadMode
  progtxt <- hGetLine handle
  hClose handle
  print "What will the program use to work during its execution?"
  print "0) Integer numbers"
  print "1) Real numbers"
  nModeString <- getLine
  let nMode = read nModeString :: Int

  print "Which execution mode do you prefer?"
  print "0) Manual execution"
  print "1) Unique test"
  print "2) Multiple test"
  eModeString <- getLine
  let eMode = read eModeString :: Int
  g <- getStdGen
  if nMode == 0
    then
      let randInt = randomRs (1,6::Int) g
          prog = read progtxt :: (Command Int)
          in (execInt eMode randInt prog)
    else
      let randDb = randomRs (1,6::Double) g
          prog = read progtxt :: (Command Double)
          in (execDb eMode randDb prog)
  return()
