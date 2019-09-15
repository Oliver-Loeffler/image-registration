# Transform types

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

