cs276-pa3
=========

To run on the training set:
bash rank.sh data/pa3.signal.train <task: cosine, bm25, or window>

To run on the development set:
In rank.sh, change the data/pa3.rel.train to data/pa3.rel.dev and then run
bash rank.sh data/pa3.rel.dev <task: cosine, bm25, or window>
