# Problems Faced

A readme file where we can put all small/big problems faced during the development of algorithm.
Can be trimmed later. 
Userful for report imo.

(1) spent much time debugging the complement given by my current algorithm implementation when the cubes are correctly covered but just more restrictive. 

(2) final code removing too much cover - imo, problem in irreduandant cover. Fixed it. I was marking cube for removal but problem comes when, ex. all 4 cubes are same; we get empty cover
Fixed it with new array that deletes the cube that is redundant(also take care of index)