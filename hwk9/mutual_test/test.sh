for ((i = 1; i <= 5; i++));do
	cat in/in$i.txt | java -jar jar/$1.jar > out/$1/out$i.txt
done
echo Done
	