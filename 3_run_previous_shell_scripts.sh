#Clean, package, build images if not present, starts dockerized app.

sh 1_clean-and-package-all.sh
sh 2_run-docker-compose.sh