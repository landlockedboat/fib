doTheThing :: [String] -> IO ()
doTheThing args = do
  let w = read (args!!1) :: Float
  let h = read (args!!2) :: Float
  let imc = (w / (h ^ 2))
  putStr $ args!!0 ++ ": "
  if imc < 18 then
    putStr "magror"
  else if imc < 25 then
    putStr "corpulencia normal"
  else if imc < 30 then
    putStr "sobrepes"
  else if imc < 40 then
    putStr "obesitat"
  else
    putStr "obesitat morbida"
  putStr "\n"
  main

main = do
  argsString <- getLine
  let args = words argsString
  if args!!0 /= "*" then
    doTheThing args
  else
    return ()
