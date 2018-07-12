data Tree a = Node a (Tree a) (Tree a) | Empty deriving (Show)

size :: Tree a -> Int
size (Node a b c) = 1 + size(b) + size(c)
size (Empty) = 0

height :: Tree a -> Int
height (Node a b c) = 1 + max (height b) (height c)
height (Empty) = 0

equal :: Eq a => Tree a -> Tree a -> Bool
equal (Node a b c) (Node d e f) =
  a == d && equal b e && equal c f
equal (Empty) (Empty) = True
otherwise = False

isomorphic :: Eq a => Tree a -> Tree a -> Bool
isomorphic (Node a b c) (Node d e f)
