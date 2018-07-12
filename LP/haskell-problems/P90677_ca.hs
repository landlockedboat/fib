myFoldl :: (a -> b -> a) -> a -> [b] -> a
myFoldl f x []      = x
myFoldl f x (y:ys)  = myFoldl f (f x y) ys

myFoldr :: (a -> b -> b) -> b -> [a] -> b
myFoldr f z []     = z
myFoldr f z (x:xs) = x `f` myFoldr f z xs

myIterate :: (a -> a) -> a -> [a]
myIterate f x = x : myIterate f (f x)

myUntil :: (a -> Bool) -> (a -> a) -> a -> a
myUntil cond f x =
  if(cond x) then
    x
  else
    myUntil cond f (f x)

myMap :: (a -> b) -> [a] -> [b]
myMap f a = [f x | x <- a]

myFilter :: (a -> Bool) -> [a] -> [a]
myFilter cond a = [x | x <- a, cond x == True]

myAll :: (a -> Bool) -> [a] -> Bool
myAll _ [] = True
myAll cond a = myFoldl (&&) True [cond x | x <- a]

myAny :: (a -> Bool) -> [a] -> Bool
myAny _ [] = True
myAny cond a = myFoldl (||) False [cond x | x <- a]

myZip :: [a] -> [b] -> [(a, b)]
myZip _ [] = []
myZip [] _ = []
myZip (a:as) (b:bs) = (a,b):myZip as bs

myZipWith :: (a -> b -> c) -> [a] -> [b] -> [c]
myZipWith f a b = [f x y | (x, y) <- myZip a b]
