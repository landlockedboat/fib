# A convex hull algorithm

I implemented a simple convex hull algorithm and made it so it can be
updated on the fly and calculated again with litte effort.

You can fiddle with it [here](https://wextia.github.io/fib-geoc-convex-hull/).

Non-clickable link: `https://wextia.github.io/fib-geoc-convex-hull/`

The program takes in a set of points `P` with points ranging from
`p_1` to `p_n`, being `n` the size of `P` and outputs a sorted set `H` of
points that forms the convex hull of the point set `P`. The convex hull
can be drawn from `H` because `H` is outputted sorted in clockwise order.

Briefly explained, the algorithm runs through this steps:

1. Sorts `P` by x coordinate
2. We initailize the `hull` stack as empty.
3. Iterates over `P` from `p_1` to `p_n` and back from `p_1` to `p_n`.
Each iteration, a point `p` is being treated and `hull` is being
constructed. Points `a` and `b` are the points corresponding to the penultimate and
last points in `hull` respectively. i.e. the second topmost and topmost points. Inside
this loop, various operations are performed:
    1. A loop is executed while the stack size is greater than two and points
  can be popped from the stack. We are always popping `b` points, that is, the last
  point of the stack, if we see that adding `c` to the convex hull makes us
  perform a left turn while building the enclosing walls of the polygon.
    2. `p` is pushed into `hull`
4. `hull` is popped to ensure that `p_0` is not present 2 times in it.

This function takes O(nlogn) time, because sorting the points is done
in O(nlogn) time and the rest of the algorithm takes less than this time
to solve the problem.
