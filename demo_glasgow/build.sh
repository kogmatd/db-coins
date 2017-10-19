#!/bin/sh

./scripts/split_sig.pl

grep -v _03_ flists/all.flst >flists/train.flst
grep    _03_ flists/all.flst >flists/test.flst

FEA.xtp ana info/HMM.cfg
HMM.xtp trn info/HMM.cfg | tee train.log

grep Correctness train.log

REC_PACKDATA.xtp rec info/recpack.cfg
REC_PACKDATA.xtp rec info/recpack.cfg -Pouttyp=F

for I in c001 c010 c200 c005 c050; do
	echo $I
	recfix -o dec model/recfix.cfg sig_ori/$I* 2>/dev/null | grep "res fst 0" | sort | uniq -c
done

