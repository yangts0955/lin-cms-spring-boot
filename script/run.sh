#bin/sh
docker login --username=小白瞎搞开发 crpi-gjiqcc10v58u83a9.cn-hangzhou.personal.cr.aliyuncs.com


# 检查 docker login 命令的退出状态
if [ $? -ne 0 ]; then
  echo "Docker login failed. Exiting..."
  exit 1
fi

# 初始化变量
CMS_JAVA_VERSION=""
CMS_VUE_VERSION=""

# 解析命令行参数
while [ $# -gt 0 ]; do
  case "$1" in
    --java-version=*)
      CMS_JAVA_VERSION="${1#*=}"
      ;;
    --vue-version=*)
      CMS_VUE_VERSION="${1#*=}"
      ;;
    *)
      printf "***************************\n"
      printf "* Error: Invalid argument.*\n"
      printf "***************************\n"
      exit 1
  esac
  shift
done

# 检查版本变量是否已设置
if [ -z "$CMS_JAVA_VERSION" ] || [ -z "$CMS_VUE_VERSION" ]; then
  echo "Error: Versions not set. Please specify both --java-version and --vue-version."
  exit 1
fi

# 使用版本变量拉取 Docker 镜像
docker pull crpi-gjiqcc10v58u83a9.cn-hangzhou.personal.cr.aliyuncs.com/qtcms/qt-cms:$CMS_JAVA_VERSION
docker pull crpi-gjiqcc10v58u83a9.cn-hangzhou.personal.cr.aliyuncs.com/qtcms/qt-cms-vue:$CMS_VUE_VERSION

docker-compose down
sh mvn clean -DskipTests=true install
docker-compose -f docker-compose.yml up --force-recreate --build -d
docker image prune --force
docker volume prune --force
