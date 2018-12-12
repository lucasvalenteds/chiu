#!/bin/sh
clean_compiled_files() {
    rm -rf ./chiu-backend/build/
    rm -rf ./chiu-backend/src/main/resources/dist/
    rm -rf ./chiu-frontend/dist/
}

compile_frontend() {
    cd ./chiu-frontend/
    npm run build
    cd ../
}

copy_frontend_to_backend() {
    cp -r ./chiu-frontend/dist/ ./chiu-backend/src/main/resources/
}

deploy_backend() {
    cd ./chiu-backend/
    ./gradlew clean shadowJar deployHeroku
    cd ..
}

clean_compiled_files
compile_frontend
copy_frontend_to_backend
deploy_backend
