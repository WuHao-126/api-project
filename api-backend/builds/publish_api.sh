 # 需要安装sshpass brew install esolitos/ipa/sshpass
LOCAL_FILE="../target/app.jar"
REMOTE_USER="root"
REMOTE_IP="101.126.87.57"
REMOTE_PORT="22"
REMOTE_PATH="/springboot/api"
IMAGES_NAME="api_backend"


echo "scp start..."
scp -o StrictHostKeyChecking=no -P "$REMOTE_PORT"  "$LOCAL_FILE" "$REMOTE_USER@$REMOTE_IP:$REMOTE_PATH"
echo "restart server... "
ssh -o StrictHostKeyChecking=no $REMOTE_USER@$REMOTE_IP -p $REMOTE_PORT "
if [[ \$(docker images -q $IMAGES_NAME) ]]; then
   echo '存在镜像'
   docker stop $IMAGES_NAME
   docker rm $IMAGES_NAME
   docker rmi $IMAGES_NAME
else
   echo '不存在镜像，打包'
fi

 cd $REMOTE_PATH
 docker build -t $IMAGES_NAME .
 docker run -d --name $IMAGES_NAME -p 8103:8103 $IMAGES_NAME
"

echo "publish success!"

sleep 5