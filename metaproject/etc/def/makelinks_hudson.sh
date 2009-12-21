#!/bin/bash
for i in `ls ../../../ano-site/etc/def/as*`
do
ln -s $i .
echo $i
done
