| Symbol | Description                      |
| ------ | -------------------------------- |
|    +   | is directly calculated by model  |
|    O   | can be approximated from results |


| Transform              | tx  | ty  | sx  | sy  | mag | ox  | oy  | ortho/rot |
| ---------------------- | --- | --- | --- | --- | --- | --- | --- | --------- |
| Rigid                  | +   | +   |     |     |     |     |     | +         |
| Affine                 | +   | +   | +   | +   | O   | +   | +   | O         |
| Similarity             | +   | +   |     |     | +   |     |     | O         |
| Non-Uniform Similarity | +   | +   | +   | +   | O   |     |     | O         |
