#!/usr/bin/python3

import os
import sys
import time

sys.path.append(os.environ['UASR_HOME']+'-py')

import isvm
#import imodsvm
import ihmm
import idnn
import iktf
import ijob
import ifdb
from ihelp import *
icnn=idnn

def svmtrn(ftrn,ftst,fea,s,kwargs={}):
    print('svm start  '+fea+'_'+s)
    csvm=isvm.trn(ftrn, fea, **kwargs)
    restrn=isvm.evl(csvm, ftrn, fea)
    restst=isvm.evl(csvm, ftst, fea)
    print('svm finish '+fea+'_'+s)
    return (csvm,restrn,restst)

def hmmtrn(ftrn,ftst,fea,s,kwargs={}):
    print('hmm start  '+fea+'_'+s)
    chmm=ihmm.trn(flst=ftrn,fea=fea,its=[0],states=3,**kwargs)
    nldtrn=ihmm.evlp(chmm,flst=ftrn,fea=fea)
    nldtst=ihmm.evlp(chmm,flst=ftst,fea=fea)
    if icfg.get('exp')=='triclass' or icfg.get('trn.regression')==True:
        print('hmm finish '+fea+'_'+s)
    else:
        res=np.array(chmm['cls'])[nldtst.argmin(axis=1)]
        icls.cmp(ftst,res,'hmm finish '+fea+'_'+s)
    return (chmm,nldtrn,nldtst)

def ktftrn(ftrn,ftst,fea,s,kwargs={}):
    print('dnn start  '+s)
    ktf = iktf.ModKeras()
    ktf.trn(ftrn, fea)
    restrn=ktf.evl(ftrn, fea, prob=True)
    restst=ktf.evl(ftst, fea, prob=True)
    print('dnn stop  ' + s)
    return (ktf.mod,restrn,restst)

if len(sys.argv)<2: raise ValueError("Usage: "+sys.argv[0]+" CFG [-n]")
icfg.Cfg(*sys.argv[1:])

ftrn=icfg.readflst('train')
ftst=icfg.readflst('test')

dmod=icfg.getdir('model')
dlog=icfg.getdir('log')

feause=['pfa','sfa','sig']
clsuse=['hmm','svm','ktf']
if not icfg.get('feause') is None: feause=icfg.get('feause').split(',')
if not icfg.get('clsuse') is None: clsuse=icfg.get('clsuse').split(',')

regression=False
maxjob=18
s = 'coins'

if '-nn' in sys.argv: raise SystemExit()

# run sessions

print("flst [%s]"%(s))
fdb=ifdb.Fdb(s)
for typ in ['sig', 'pfa']:
    fdb.analyse(typ, eval(typ+'get'))

sfaget(ftrn, ftst, fdb)
fdb.save()
ftrn=ftrn.equalcls()

for cls in clsuse:
    for fea in feause:
        if cls=='hmm' and ftrn[0][fea].shape[-1]>40 :
            continue
        resfn=os.path.join(dlog,'res_'+cls+'_'+fea+'_'+s+'.npy')
        if os.path.exists(resfn):
            continue
        print('####################',fea,cls,s,'####################')

        kwargs=icfg.get('trnargs.%s.%s'%(cls,fea))

        if kwargs is None:
            if cls=='dnn' or cls[:3]=='cnn': continue
            kwargs={}
        else:
            print('trnargs = '+kwargs)
            kwargs=eval(kwargs)
        kwargs['regression']=regression

        fnctrn=eval(cls[:3]+'trn')

        for i in range(3):
            (mod,restrn,restst)=fnctrn(ftrn,ftst,fea,s,kwargs)
            if len(restst)>0:
                np.save(resfn,restst)
                np.save(resfn[:-4]+'_trn.npy',restrn)
                eval('i'+cls[:3]+'.save')(mod,resfn[:-4]+'.model')
                break



#job=ijob.Job(maxjob)

#if os.path.exists('stop'): job.cleanup(); raise SystemExit()
#job.start('run_'+s,run,(s,))
#job.res('run_'+s)

raise SystemExit()

