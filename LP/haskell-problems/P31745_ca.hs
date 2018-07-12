flatten :: [[Int]] -> [Int]
flatten x = foldl (++) [] x

myLength :: [Char] -> Int
myLength x = foldl (+) 0 (map (const 1) x)

myReverse :: [Int] -> [Int]
myReverse x = foldr (flip (++)) [] (map (:[]) x)

countOcc :: Int -> [Int] -> Int
countOcc n x = sum(map(const 1)(filter (== n) x))

countIn :: [[Int]] -> Int -> [Int]
countIn x n = map (countOcc n) x

firstWord :: String -> String
firstWord x = takeWhile (/=' ') $ dropWhile (==' ') x
