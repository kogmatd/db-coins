#!/bin/bash

DBHOME=$UASR_HOME-data/coins

cd $DBHOME/Versuch1;   nohup HMM.xtp trn info/Versuch1.mcfg   2>&1 &
cd $DBHOME/Versuch2;   nohup HMM.xtp trn info/Versuch2.mcfg   2>&1 &
cd $DBHOME/Versuch3;   nohup HMM.xtp trn info/Versuch3.mcfg   2>&1 &
cd $DBHOME/Vorversuch; nohup HMM.xtp trn info/Vorversuch.mcfg 2>&1 &
