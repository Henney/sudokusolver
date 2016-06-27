# sudokusolver

The Java code in this project handles sudoku solving and generation amongst other things. It was written for a second year project for our bachelor in software engineering. The code is documented with a report in Danish.

The SAT solver Plingeling can be found at http://fmv.jku.at/lingeling/ and should be installed and included in environment variables in order to solve sudokus using SAT.
Jaunt can be found at http://jaunt-api.com/ and must be updated once a month in order to fetch sudokus online.

The program can run without either.

Abstract:

This report shows how to solve sudokus of arbitrary size using two different approaches.
First using our own solver in Java employing constraint propagation through implemented
tactics, a customised priority queue, and other customized data structures. Second using
the SAT-solver Plingeling by encoding the rules of sudoku in CNF. Both solvers are
used to generate new puzzles utilising a Las Vegas algorithm. All of the functionality is
exposed through a user-friendly graphical user interface including tests for solvability,
uniqueness of puzzles, and visual display of the solving process. Our implementation is
thoroughly tested, and we provide benchmarks of the runtimes of different tactics and a
comparison of the solvers.
Our results show that using few tactics, ones we call Unique Candidate and Incremental
Naked Pair, we can solve sudokus most effectively, spending less than a millisecond on
arguably difficult classical sudokus. Our tactic solver is faster for smaller puzzles, whereas
Plingeling has an advantage on larger ones. We also find that puzzle generation is possible
within reasonable time for smaller puzzle sizes, taking around 10 seconds for puzzles of
size 16 × 16 and 400 seconds for size 25 × 25.
