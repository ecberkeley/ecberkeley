#!/bin/bash

cp -r  ~/doc.lar/projects/charlie/tapes/dynamide/build/ROOT/homes/dynamide/assemblies/com-dynamide-apps-1/apps/tagonomy/resources/static/minesweeper3d  .

# This file pulls from the tagonomy site under local dynamide and puts them here.
# It will be of limited use if you ever change the location of the tagonomy dev folder, 
# so you'll want to edit pull.bash.


rm minesweeper3d.zip; find minesweeper3d |grep -v '.svn' | zip minesweeper3d.zip -@

scp minesweeper3d.zip laramiessh@laramiecrocker.com:~/sites/demo.laramiecrocker.com

svn commit minesweeper3d

bsync

