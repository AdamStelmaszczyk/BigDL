#!/bin/bash
SGX=1 ./pal_loader bash -c "/opt/jdk8/bin/java -cp \
  '/ppml/trusted-big-data-ml/work/bigdl-2.1.0/jars/*:/ppml/trusted-big-data-ml/work/spark-3.1.2/conf/:/ppml/trusted-big-data-ml/work/spark-3.1.2/jars/*' \
  -Xmx2g \
  org.apache.spark.deploy.SparkSubmit \
  --master 'local[4]' \
  --conf spark.driver.memory=2g \
  --conf spark.executor.extraClassPath=/ppml/trusted-big-data-ml/work/bigdl-2.1.0/jars/* \
  --conf spark.driver.extraClassPath=/ppml/trusted-big-data-ml/work/bigdl-2.1.0/jars/* \
  --properties-file /ppml/trusted-big-data-ml/work/bigdl-2.1.0/conf/spark-analytics-zoo.conf \
  --jars /ppml/trusted-big-data-ml/work/bigdl-2.1.0/jars/* \
  --py-files /ppml/trusted-big-data-ml/work/bigdl-2.1.0/python/bigdl-orca-spark_3.1.2-2.1.0-python-api.zip \
  --executor-memory 2g \
  /ppml/trusted-big-data-ml/work/examples/pyzoo/xgboost/xgboost_classifier.py \
  -f path_of_pima_indians_diabetes_data_csv" | tee test-xgboost-classifier-sgx.log
