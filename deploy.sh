projectPath=
warPath=
deployPath=
echo '项目路径:$projectPath'
echo 'War包路径:$warPath'
echo '部署路径:$deployPath'
cd $projectPath
echo '更新代码'
git pull origin master
echo '准备打包'
mvn clean package
echo '打包完成'
echo '删除项目中的war'
rm -rf $deployPath
echo '正在部署'
mv $warPath $deployPath
echo '部署完成'