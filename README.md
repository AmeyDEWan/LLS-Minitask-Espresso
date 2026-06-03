# Espresso

This repository contains the first Mini-Task of Amey Wankhede and Sebastian Engel in the course Low Level Synthesis at the Rechnersysteme group of TU Darmstadt. The Mini-Task is supervised by M.Sc. Katharina Schultheis.

To clone this repository use:

```
git clone git@git.rwth-aachen.de:sebastian.engel.20/lls-minitask-1.git
```

The implementation can be found in `./espresso` and the latex files of the report can be found in `./report`.
Notice that the task description is available under `./task_description_espresso.pdf`.


## What we are doing?
(would love ur input Sebastian)

- We are performing logic minimization
- the data within .blif file is a PLA structure; with lines representing logic such as 100, 11-, 0-1, etc.
- In the framework we are using, each line is a Cube object.
    - 1 means input must be true
    - 0 means input must be false
    - - means input is don't care(could be either true or false)
    - EX. 10- means first input is 1, second input is 0, third input is "don't care" meaning 10- covers both the logic states 100 and 101
- Objective : Write a Java code that takes a `List<Cubes>` as input and outputs a minimized `List<Cubes>` that contains fewer cubes, implying less wires + less logic blocks, further implying faster circuit + lower cost + lower size
- We iteratively do the following 
    - Reduce - Reduce the On Set,  shrinking terms by converting `-` into either 1 or 0. This reduces overlap + gives breathing space required to expand cube in further directions.
    - Expand - Take newly shrunken cubesets from reduce step, and change 1/0's into - to expand them into new directions; all while keeping track of Off-set intersections so that no boundary is crossed
    - Irredundant - Remove any ON-Set covered by ther ON-sets

```
List<Cube> onSet 
List<Cube> offSet = calculateComplement(onSet)

int prevSize
int curSize = onSet.size()

do {
    prevSize = curSize;

    onSet = reduce(onSet);
    onSet = expand(onSet, offSet);
    onSet = irredundant(onSet);

    curSize = onSet.size();
} while (curSize < prevSize) // or any other criteria?
```

## Tasks description

1. Transform BLIF into Positional Cube Notation(PCN)
    - A framework provided in "self-assesment" is utlized here
    - [x] Transform BLIF into PCN (currently handled by Cube.java)

[ ] Map following steps into functions for better tracability

2. Algortihm Implementation - 
    - [ ] Compute function complement (Pre-Computed = Not part of Espresso minimiation loop) -> Derive a efficient way to calculate the OFF-SET from the ON-SET. The OFF-Set is required to enforce strict boundary during the expansion phase of Espresso algorithm. Could be implemented as a `Class Complement`
    - [ ] Reduce - A method that takes an On-Set and spits out reduced On-Set that is still valid(removes overlaps)
    - [ ] Expand - A method that takes an On-Set & OFF-set; tries out new VALID directions to expand in; also implementing heuristic as in which cubes to expand(based on size/weight?)
    - [ ] Irredundant - A method that throws away On-Set that's already been covered by other On-Sets
    - [ ] A loop, with valid conditions, that runs the reduce,expand,irredundant
    - [ ] Evaluate using ABC


