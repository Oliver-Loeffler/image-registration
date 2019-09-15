| Legend                                         |
| ---------------------------------------------- |
| + is directly calculated by model              |
| O can be derived/approximated from results     |


| Transform              | tx  | ty  | sx  | sy  | mag | ox  | oy  | ortho/rot |
| ---------------------- | --- | --- | --- | --- | --- | --- | --- | --------- |
| Rigid                  | +   | +   |     |     |     |     |     | +         |
| Affine                 | +   | +   | +   | +   | o   | +   | +   | o         |
| Similarity             | +   | +   |     |     | +   |     |     | o         |
| Non-Uniform Similarity | +   | +   | +   | +   | o   |     |     | o         |
