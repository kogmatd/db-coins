#!/bin/bash

DBHOME=$UASR_HOME-data/coins

cd $DBHOME/Versuch1;   nohup FEA.xtp ana info/Versuch1.mcfg   2>&1 &
cd $DBHOME/Versuch2;   nohup FEA.xtp ana info/Versuch2.mcfg   2>&1 &
cd $DBHOME/Versuch2;   nohup FEA.xtp ana info/Versuch3.mcfg   2>&1 &
cd $DBHOME/Vorversuch; nohup FEA.xtp ana info/Vorversuch.mcfg 2>&1 &
