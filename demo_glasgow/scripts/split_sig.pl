#!/usr/bin/perl

use strict;
use File::Basename;

my $defcfg=$ENV{HOME}."/src/misisdemo/bsp/coinsall.cfg";
my $sigori=dirname($0)."/../sig_ori";
my $sigdst=dirname($0)."/../sig";
my $flst=dirname($0)."/../flists/all.flst";
my $seglen=150;
my $srate=48000;
my $crate=160;
my $wlen=400;

system "rm -rf $sigdst";
mkdir $sigdst;

opendir DIR,$sigori;
my @sigori=readdir DIR;
closedir DIR;
open FLST,">".$flst;
foreach my $fn (@sigori){ &split($sigori."/".$fn,$sigdst."/".$fn); }
close FLST;


sub split {
	(my $fsrc,my $fdst)=@_;
	my @seg=();
	open FD,"recfix -o dec \"$defcfg\" \"$fsrc\" 2>/dev/null |";
	while(<FD>){ push @seg,$1 if $_=~/dec_init frm ([0-9]+)/; }
	close FD;
	foreach my $ini (@seg){
		my $s0=$ini*$crate/$srate;
		my $len=(($seglen-1)*$crate+$wlen)/$srate;
		next if $s0+$len>30;
		my $fd=$fdst;
		$ini=sprintf "%05i",$ini;
		$fd=~s/\.wav/_$ini\.wav/;
		my $cmd=sprintf "sox \"%s\" \"%s\" trim %.6f %.6f",
			$fsrc,$fd,$s0,$len;
		print $cmd."\n";
		system $cmd;
		$fd=basename($fd);
		$fd=~s/\.wav//;
		my $cls=uc $fd; $cls=~s/_.*$//;
		print FLST $fd."\t".$cls."\n";
	}
}

