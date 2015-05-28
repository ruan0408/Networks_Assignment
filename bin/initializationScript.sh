xterm -hold -title "Peer 1" -e "java cdht 1 3 7" &
xterm -hold -title "Peer 3" -e "java cdht 3 7 8" &
xterm -hold -title "Peer 7" -e "java cdht 7 8 9" &
xterm -hold -title "Peer 8" -e "java cdht 8 9 10" &
xterm -hold -title "Peer 9" -e "java cdht 9 10 12" &
xterm -hold -title "Peer 10" -e "java cdht 10 12 15" &
xterm -hold -title "Peer 12" -e "java cdht 12 15 1" &
xterm -hold -title "Peer 15" -e "java cdht 15 1 3" &