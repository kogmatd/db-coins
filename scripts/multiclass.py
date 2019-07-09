#!/usr/bin/python3
# Script for Multiclass Experiments

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
    chmm=ihmm.trn(flst=ftrn, fea=fea, its=[3, 5, 7, 5], states=5, **kwargs)
    #chmm = ihmm.trn(flst=ftrn, fea=fea, its=[3, 5, 7, 9, 11, 13, 7, 7], states=9, **kwargs)
    nldtrn=ihmm.evlp(chmm, flst=ftrn, fea=fea)
    nldtst=ihmm.evlp(chmm, flst=ftst, fea=fea)
    if icfg.get('exp')=='triclass' or icfg.get('trn.regression') == True:
        print('hmm finish '+fea+'_'+s)
    else:
        print('hmm finish ' + fea)
        res_trn = np.array(chmm['cls'])[nldtrn.argmin(axis=1)]
        icls.cmp(ftrn, res_trn, 'Training set ' + fea)
        res_tst = np.array(chmm['cls'])[nldtst.argmin(axis=1)]
        icls.cmp(ftst, res_tst, 'Test set ' + fea)
    return (chmm, nldtrn, nldtst)

def ktftrn(ftrn,ftst,fea,s,kwargs={}):
    print('dnn start  '+s)
    config = dict()
    config['batchsize'] = 256
    config['epochs'] = 50
    config['lay'] = [('relu',600),('batch',), ('dropout',0.4), ('relu',300),('batch',),('dropout',0.4)]
    ktf = iktf.ModKeras(**config)
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
fdb=ifdb.Fdb()
for typ in ['sig', 'pfa']:
    fdb.analyse(typ, eval(typ+'get'), ftrn+ftst)

sfaget(ftrn, ftst, fdb)
fdb.save()

'''Not for highly unbalanced datasets'''
#ftrn=ftrn.equalcls()

for cls in clsuse:
    for fea in feause:
        if cls=='hmm' and ftrn[0][fea].shape[-1]>40 :
            continue
        resfn=os.path.join(dlog,'res_'+cls+'_'+fea+'_'+s+'.npy')
        #if os.path.exists(resfn):
            # continue
        print('####################', fea, cls, s,'####################')

        kwargs = icfg.get('trnargs.%s.%s' %(cls,fea))

        if kwargs is None:
            if cls == 'dnn' or cls[:3] == 'cnn': continue
            kwargs = {}
        else:
            print('trnargs = '+kwargs)
            kwargs = eval(kwargs)

        kwargs['regression'] = regression

        fnctrn= eval(cls[:3]+'trn')

        (mod, restrn, restst) = fnctrn(ftrn, ftst, fea, s, kwargs)

        if len(restst) > 0:
            np.save(resfn[:-4]+'_tst.npy', restst)
        if len(restrn) > 0:
            np.save(resfn[:-4]+'_trn.npy', restrn)
        if mod is not None:
            modfn = os.path.join(dmod, cls + '_' + fea + '_' + s + '.model')
            eval('i'+cls[:3]+'.save')(mod, modfn)
        break

raise SystemExit()
