cd $(dirname $0)
curl -v -F "file=@myfile.txt" "localhost:8080/file"