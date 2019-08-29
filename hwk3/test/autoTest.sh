a=1
cat input | while read line
do
    echo "[ Test sample $a ]"
    echo -e "Input:\n$line \n"

    output1=$(echo $line | python3 diff.py)
    echo -e "SymPy output:\n$output1 \n"

    cd users/$1
    output2=$(echo $line | java $2)
    echo -e "$1 output:\n$output2 \n"
    cd ..
    cd ..
    
    result=$(echo -e "$output1 \n$output2" | python3 compare.py)
    echo -e "[ $result ]"
    echo "-------------------------------------------------------"

    a=`expr $a + 1`
done

echo Done

