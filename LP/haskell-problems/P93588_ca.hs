myMap :: (a -> b) -> [a] -> [b]
myMap f x = [ f e | e <- x ]

myFilter :: (a -> Bool) -> [a] -> [a]
myFilter f x = [ e | e <- x, f e ]

myZipWith :: (a -> b -> c) -> [a] -> [b] -> [c]
myZipWith f xs ys = [ f x y | (x,y) <- zip xs ys ]

--donades dues llistes d’enters, genera la llista que aparella els elements si
--l’element de la segona llista divideix al de la primera.
thingify :: [Int] -> [Int] -> [(Int, Int)]
thingify xs ys = [(x,y) | x <- xs, y <- ys, mod x y == 0]

factors :: Int -> [Int]
factors n = [x | x <- [1..n], mod n x == 0]
