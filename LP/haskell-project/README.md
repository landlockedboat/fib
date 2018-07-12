# The statement: reduced (and in :uk:) version

Amazing haskell project :robot: wip for the programming languages course @ my uni.

I have to implement an interpreter and a tester for a simple imperative language using [haskell](https://www.haskell.org/) and [pccts](http://www.antlr2.org/pccts133.html).

## Language description

### Sample code

```
INPUT X
INPUT Y
IF X > 0 OR X = 0 OR NOT 0 > Y THEN
  Z := 1
  WHILE X > Y
  DO
    X := X - 1
    Z := Z * Z
  END
ELSE
  Z := 0
END
PRINT Z
```

***

```
INPUT X
EMPTY P
WHILE X > 0 OR X = 0
DO
  INPUT Y
  PUSH P Y
END
S := 0
SIZE P L
WHILE L > 0
DO
  POP P Y
  S := S + Y
  L := L - 1
END
PRINT S
```
### Considerations

All variables are represented by an indentifier `Ident`. `Ident` is a `String`.

The language is made for executing its programs over an entry list `INL` that is of type `a`, returning an exit list `OUL` with values or an error message, being of type `String`.

### Instruction breakdown


* `INPUT Ident`: Grabs the first value from `INL` and puts it into the variable named `Ident` The pointer to `INL` is then incremented.

* `PRINT Ident`: Puts the value of the variable `Ident` on `OUL`, appended to the end of it as a `String`.

* `Ident := (Ident | Num)`: Assigns to the variable `Ident` the value of the second `Ident` variable or the numerical value `Num`.

* `EMPTY Ident`: Assigns to `Ident` an empty list.

* `SIZE Ident Ident`: Puts the size of the list `Ident` on the second `Ident`. If the first variable `Ident` is not a list, the error `type error` will be generated.

* `PUSH Ident (Ident | Num)`: Adds the content of the variable `Ident` or a numerical value `Num` to the top of the list `Ident`. `Ident` increases its size by one as a result. `Ident` has to be a list for this to work, otherwise the error `type error` will be generated.

* `POP Ident Ident`: Eliminates the element from the top of the stack `Ident` and puts it into the second `Ident`. The stack `Ident` decreases its size by one as a result. If this operation is to be performed on an empty stack the error `empty stack` is then generated.

* `BExpr OR BExpr`: Evaluates to `BExpr || BExpr` in C.

* `BExpr AND BExpr`: Evaluates to `BExpr && BExpr` in C.

* `NOT BExpr`: Evaluates to `!BExpr` in C.

* `(Ident | Num) > (Ident | Num)`: Returns a `Boolean`. Evaluates to the same in C.

* `(Ident | Num) = (Ident | Num)`: Returns a `Boolean`. Evaluates to `(Ident | Num) == (Ident | Num)` in C.

* `(Ident | Num) + (Ident | Num)`: Returns a numeric value. Evaluates to the same in C.

* `(Ident | Num) - (Ident | Num)`: Returns a numeric value. Evaluates to the same in C.

* `(Ident | Num) * (Ident | Num)`: Returns a numeric value. Evaluates to the same in C.

* `IF BExpr THEN instructions [ELSE instructions] END`: the instructions `instructions` are executed if `BExpr` is true. Otherwise, and if an `ELSE` directive is found at the same level of the first `IF` directive, the set of instructions `instructions` following the `ELSE` will be executed.

* `WHILE BExpr DO instructions END`: Executes `instructions` while `BExpr` evaluates to `true`.

## Haskell data AST generation

I have to be sure to follow the *following* considerations regarding code stuff:

1. Define a *polymorphic* generic data type named `Command a` that enables me to work with every possible data type.

  It has to be able to represent within the language:

    * The *assignation* using the `Assign` constructor.
    * *Input* directives with `Input`.
    * *Print* instructions with `Print`.
    * *Stack operations* such as `Empty`, `Push`, `Pop` and `Size`.

  It has to be able to apply the following to itself:

    * The *sequential composition* as a `list` of `Command` with the `Seq` constructor.
    * The *conditional* with the `Cond` constructor.
    * The *iteration* with the  `Loop` constructor that uses boolean expressions.

2. Define a *polymorphic* generic data type named `BExpr` that has the following attributes:

  It can take the form of `AND`, `OR` and `NOT`, having its constructors the same name.

  It can take the form of the *relational comparators* `>` and `=` being its constructors `Gt` and `Eq` respectively. This comparators can only be applied to *numerical expressions*.

  This expresions will *not* have any parenthesization.

  They hasve to be evaluated from left to right, without regards to operation priority.

3. Define a *polymorphic*(?) generic data type named `NExpr` that has the following attributes:

  It can take the form of:

    * A *variable* using the `Var` constructor.
    * A *constant* using the `Const` constructor.
    * A *sum* operator `+` using the `Plus` constructor.
    * A *substraction* operator `-` using the `Minus` constructor.
    * A *product* operator `*` using the `Times` constructor.

  This expresions will *not* have any parenthesization.

  It has to be evaluated following the normal operation priority (product -> sum -> substraction).

4. Back to the `Command` class:

I have to define a function inside `Command` as an instance of the `Show` class, in a way that when executing `putStr` of `show` it will print the code inside `Command` with an identation (2 whitespaces per each identation level) like the one on the examples.

## Reading programs

Make a grammar using `PCCTS` :moyai: and adapt the `printAST` function for generating an expression that a derivation of `Read` implemented by `Command` can read and store in itself.

I have to make a little script for outputting the results of `printAST` to the `haskell-project` executable.

## Interpreter

Some class names conventions and obligatory implementations to be done when coding the interpreter:

  1. Define in *Haskell* a new data named `SymTable a` that lets us mantain and query the values that the variables contain and know its type.

  Note that we have variables of different types: either they are `a` or stacks of `a`. All variables are global and, thus, each of them can only have one unique type.

  2. Define in *Haskell* a new data named `Evaluable` of type `e` that represents expresstions. Note that `e` is a container as in `Functor` or `Monad`.

    This class will have the following operations:

      * `eval :: (Num a, Ord a) => (Ident -> a) -> (e a) -> (Either String a)`
      * `typeCheck :: (Ident -> String) -> (e a) -> Bool`

    Make it so that `NExpr` and `BExpr` are instances of the `Evaluable` class:

      * I have to create two functions, one that evaluates boolean expressions and one that evaluates numerical expressions.

      * The evaluation has to return an error if any of the variables is undeclared or it has an incorrect type.

  3. Create a function `interpretCommand :: (Num a, Ord a) => SymTable a -> [a] -> Command a -> ((Either String [a]),Mem a, [a])` that interprets an `AST` for a given memory and `INL`.

    It returns a triplet containing:

      1. The first element of the `OUL` (or an error message).

      2. The resulting memory after the execution.

      3. The `INL` after the execution.

  4. Using the previous function create a new function `interpretProgram:: (Num a,Ord a) => [a] -> Command a -> (Either String [a])` that evaluates a complete piece of code given an input.

  5. Final consideration: Every program or expression that contains a subexpression that generates an error during evaluation has to return an error too.

    The following kinds of errors have to be reported:

      * `undefined variable`
      * `empty stack`
      * `type error`

## Final notes (by Jordi Petit)

* If you are not feeling confident, implement your data structures for `Int`s first, then make a generalisation.
* Write some instructions down for compiling and executing everything
* If you want your project to be graded with care, make it carefully.
* Make test cases
* Make comments!
* Name functions with beautiful names
* If a functionality's not been implemented, say it out loud!
