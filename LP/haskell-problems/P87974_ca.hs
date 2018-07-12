main = do
  name <- getLine
  if elem (last name) "aA" then
    putStrLn "Hola maca!"
  else
    putStrLn "Hola maco!"
