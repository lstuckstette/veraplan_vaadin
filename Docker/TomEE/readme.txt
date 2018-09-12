1. Install Docker : 
    https://store.docker.com/editions/community/docker-ce-desktop-windows
2. Build image
    cd Docker/TomEE
    docker build -t vp_tomee_image .
3. start image 
    docker run -d -p 8888:8888 -p 8080:8080 --name vp_tomee vp_tomee_image
4. Start/Stop Container
    docker start vp_tomee
    docker stop vp_tomee
5. Remove Container
    docker rm vp_tomee