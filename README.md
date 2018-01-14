```
cat <<EOF > Bootstrap.sh
#!/bin/bash
sudo yum install -y java-1.8.0-* git gcc-c++ make
sudo yum remove -y java-1.7.0-*
curl --silent --location https://rpm.nodesource.com/setup_6.x | sudo bash -
sudo yum install mysql -y
sudo pip install faker
sudo pip install --egg mysql-connector-python-rf
cd /home/ec2-user
wget http://mirrors.whoishostingthis.com/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.zip
unzip apache-maven-3.3.9-bin.zip
echo "export PATH=\$PATH:/home/ec2-user/apache-maven-3.3.9/bin" >> .bashrc
git clone https://github.com/rirakuchell/KCLWithPropertiesFile.git
mkdir ./KCLWithPropertiesFile/logs
chown -R ec2-user ./KCLWithPropertiesFile
EOF
```

```
cd ~/KCLWithPropertiesFile
mvn clean compile assembly:single
```

```
nohup bash -c \
"(java -cp ./target/KCL-CommonProcessor-Propertiesfile-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.amazonaws.services.kinesis.kclapp.processor.ProcessorWithProperties > ~/KCLWithPropertiesFile/logs/ProcessorWithProperties.log) \
 &> ~/KCLWithPropertiesFile/logs/ProcessorWithProperties.log" &
 ```