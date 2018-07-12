myLength :: [Int] -> Int
myLength x = sum (map (const 1) x)

myMaximum :: [Int a] -> Int
myMaximum a = a
myMaximum x:xs = if (x > myMaximum xs) then x else myMaximum xs
