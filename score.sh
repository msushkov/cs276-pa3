#!/usr/bin/env sh
# ./score.sh <ranked.txt> <2013.data/queryDocTrainRel>
java -Xmx1024m -cp bin/ edu.stanford.cs276.NdcgMain $1 $2
