## Transform types

| Symbol | Description                                                      |
| ------ | ---------------------------------------------------------------- |
|    +   | is directly calculated by model                                  |
|    o   | can be approximated from results                                 |
|  sx/sy | anisotropic scaling (scale x/y)                                  |
|   mag  | magnification, isotropic scaling (mag = average(sx,sy))          |
|  ox/oy | anisotropic rotation (shearing or non-orthogonality, ortho x/y)  |
|  ortho | isotropic non-orthogonality (ortho = oy - ox)                    |
|   rot  | isotropic rotation                                               |
|  tx/ty | translation in x/y direction                                     |


| Transform              | tx  | ty  | sx  | sy  | mag | ox  | oy  | ortho/rot |
| ---------------------- | --- | --- | --- | --- | --- | --- | --- | --------- |
| Rigid                  | +   | +   |     |     |     |     |     | +         |
| Affine                 | +   | +   | +   | +   | o   | +   | +   | o         |
| Similarity             | +   | +   |     |     | +   |     |     | +         |
| Non-Uniform Similarity | +   | +   | +   | +   | o   |     |     | +         |


## Cases

* References (Refs): X, Y
* Readings (Readings): Xd, Yd

### #1 Common case, references and readings in 2D

| Direction | Refs | Readings |
| --------- | -----| -------- |
| X (count) | > 1  | > 1      |
| Y (count) | > 1  | > 1      |

### #2 special case, references 2D, readings 1D, x

| Direction | Refs | Readings |
| --------- | -----| -------- |
| X (count) | > 1  | > 1      |
| Y (count) | > 1  | = 0      |

### #3 special case, references 2D, readings 1D, y

| Direction | Refs | Readings |
| --------- | -----| -------- |
| X (count) | > 1  | = 0      |
| Y (count) | > 1  | > 1      |
