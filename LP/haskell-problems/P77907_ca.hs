absValue :: Int -> Int

absValue x
  | x < 0       = -x
  | otherwise   = x

power :: Int -> Int -> Int

power x 0 = 1
power x y = x * power x (y-1)

isPrime :: Int -> Bool
isPrime 1 = True
isPrime n = isPrime' n (n-1)
  where
    isPrime' :: Int -> Int -> Bool
    isPrime' x x'
      | x' == 1           = True
      | mod x x' == 0     = False
      | otherwise         = isPrime' x (x' -1)

slowFib :: Int -> Int
slowFib n
  | n == 0      = 0
  | n == 1      = 1
  | otherwise   = slowFib (n-1) + slowFib (n-2)

quickFib :: Int -> Int
quickFib n = fst(quickFib' n)
  where
    quickFib' :: Int -> (Int, Int)
    quickFib' 0 = (0, 0)
    quickFib' 1 = (1, 0)

    quickFib' n = (fn1 + fn2, fn1)
      where (fn1, fn2) = quickFib' (n-1)
