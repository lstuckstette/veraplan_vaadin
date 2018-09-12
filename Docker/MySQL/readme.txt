1. Install Docker : 
    https://store.docker.com/editions/community/docker-ce-desktop-windows
2. Pull mysql-image
    docker pull mysql
3. Build image
    cd ./Docker/MySQL
    docker build -t veraplan_mysql
4. start image 
    docker run -d -p 3306:3306 veraplan_mysql
4. Start/Stop Container
    docker start veraplan_mysql
    docker stop veraplan_mysql
5. Remove Container
    docker rm veraplan_mysql