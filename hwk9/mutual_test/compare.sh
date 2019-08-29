for ((i = 1; i <= 5; i++));do
    echo In out$i.txt:
    diff out/my/out$i.txt out/$1/out$i.txt
done
echo Done