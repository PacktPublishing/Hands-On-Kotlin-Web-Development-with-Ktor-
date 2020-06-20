cd $(dirname $0)
#docker run -i loadimpact/k6 run --vus 5 --duration 20s - <script.js
docker run -i --network host loadimpact/k6 run - </home/mike/IdeaProjects/packt-ktor/bookstore/script/performance/script.js