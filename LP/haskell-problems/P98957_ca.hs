-- Llistes infinites
ones :: [Integer]
ones = iterate (id) 1

nats :: [Integer]
nats = iterate (+1) 0

ints :: [Integer]
ints = iterate ifunc 0
  where
    ifunc :: Integer -> Integer
    ifunc n
      | n > 0 = neg n
      | otherwise = (neg n) + 1
      where
        neg n = n * (-1)

triangulars :: [Integer]
triangulars = scanl scanf 0 (iterate (+1) 1)
  where
    scanf ant curr = ant + curr

factorials :: [Integer]
factorials = scanl scanf 1 (iterate (+1) 1)
  where
    scanf ant curr = curr * ant

fibs :: [Integer]
fibs = [1,1] ++ __fibs 1 1
  where
    __fibs :: Integer -> Integer -> [Integer]
    __fibs i1 i2 = (i1+i2):(__fibs i2 (i1+i2))

primes :: [Integer]
primes = [x | x <- nats, isPrime x]
  where
    isPrime n
      | n == 0 = False
      | n == 1 = True
      | otherwise = __isPrime n (n-1)
      where
        __isPrime x di
          | di == 1           = True
          | mod x di == 0     = False
          | otherwise         = __isPrime x (di -1)

hammings :: [Integer]
hammings = map mfunc (iterate (+1) 3)
  where
    mfunc :: Integer -> Integer
    mfunc n
      | mod n 3 == 0 = (div n 3) * 2
      | mod n 3 == 1 = (div n 3) * 3
      | mod n 3 == 2 = (div n 3) * 5
