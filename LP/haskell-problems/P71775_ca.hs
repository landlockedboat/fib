countIf :: (Int -> Bool) -> [Int] -> Int
countIf f a = sum [1 | x <- a, f x]

pam :: [Int] -> [Int -> Int] -> [[Int]]
pam a f = [ wop a x | x <- f] where
  wop i f = [f x | x <- i]

pam2 :: [Int] -> [Int -> Int] -> [[Int]]
pam2 a f = [ wop x f | x <- a] where
  wop i f = [x i | x <- f]

filterFoldl :: (Int -> Bool) -> (Int -> Int -> Int) -> Int -> [Int] -> Int
filterFoldl c f i l@(x:xs) = foldl f i (filter c l)

insert :: (Int -> Int -> Bool) -> [Int] -> Int -> [Int]
insert f l@(x:xs) a
  | null l = [a]
  | not (f x a) = a:l
  | null xs = x:[a]
  | (f x a) && not (f (head xs) a) = x:a:xs
  | otherwise = x:(insert f xs a)

insertionSort :: (Int -> Int -> Bool) -> [Int] -> [Int]
insertionSort f l@(x:xs) = 
