import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import luzu.test.J;
import luzu.test.Person;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaUtils;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.*;
import org.apache.spark.sql.cassandra.CassandraSQLContext;
import org.apache.spark.sql.execution.datasources.jdbc.JDBCRDD;
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.receiver.Receiver;
import org.apache.spark.util.SystemClock;
import scala.Function1;
import scala.runtime.AbstractFunction1;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapRowTo;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import static org.apache.spark.sql.functions.*;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * Created by zulk on 04.02.16.
 */
public class JdbcSparkTest implements Serializable{

    class Data implements Serializable{
        String name;
        String name1;
        int id;

        public Data(String name, String name1, int id) {
            this.name = name;
            this.id = id;
            this.name1 = name1;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "name='" + name + '\'' +
                    ", name1='" + name1 + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

    private  void spark() throws ExecutionException, InterruptedException {
        Logger.getLogger("org").setLevel(Level.INFO);
        String           logFile = "/var/log/*.log"; // Should be some file on your system
        SparkConf        conf    = new SparkConf()
                .setAppName("Simple Application")
//                .setMaster("spark://192.168.100.105:7077")
                .setMaster("local[*]")
                .set("spark.cassandra.connection.host", "127.0.0.1")
                .setJars(new String[] {"/home/zulk/bin/javalib/postgresql-9.4.1207.jar"});
        JavaSparkContext sc      = new JavaSparkContext(conf);
//        JavaRDD<String>  logData = sc.textFile(logFile).cache();


        CassandraSQLContext cassandraSQLContext = new CassandraSQLContext(sc.sc());
        DataFrame cassandraDF = cassandraSQLContext.cassandraSql("select a1.id_ala,a1.dd,a,date,val from kkk.ala1 a1 join kkk.ala2 a2 on a1.id_ala = a2.id_ala");
        cassandraDF.show();
        cassandraDF.registerTempTable("ala1");


        SQLContext sqlContext = new SQLContext(sc);

        DataFrame brdDataFrame = sqlContext.createDataFrame(ImmutableList.of(new Data("namne", "val", 1)), Data.class);
        brdDataFrame.registerTempTable("brd_temp");
//        Broadcast<DataFrame> broadcast = sc.broadcast(brdDataFrame);

//        try {
//            Class.forName("org.postgresql.Driver");
//            System.out.println("PostgreSQL JDBC Driver Registered!");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }



        DataFrame testDF = sqlContext.read().format("jdbc").options(
                ImmutableMap.of("url","jdbc:postgresql://192.168.100.105:5432/ala?user=zulk&loglevel=2",
                                "dbtable","(select max(n1) as maxid from test) tmp",
                                "driver","org.postgresql.Driver")).load();


        Row[] maxIds = testDF.select("maxid").collect();
        Integer testMax     = (Integer) maxIds[0].get(0);

        Map<String, String> options = new HashMap<String, String>();
        options.put("url", "jdbc:postgresql://192.168.100.105:5432/ala?user=zulk&loglevel=2");
        options.put("dbtable", "test");
        options.put("driver", "org.postgresql.Driver");
        options.put("partitionColumn", "n1");
        options.put("lowerBound", "0");
//        options.put("spark.sql.autoBroadcastJoinThreshold","50485760");
        options.put("upperBound", Integer.toString(testMax));
        options.put("numPartitions","2");

        System.out.println("MAX: "+testMax);

        DataFrame dataFrame = sqlContext.read().format("jdbc").options(options).load();

        dataFrame.printSchema();
        Dataset<Person> personDataset = dataFrame.as(Encoders.bean(Person.class));

        personDataset.show();
        CassandraJavaUtil.javaFunctions(personDataset.rdd())
                .writerBuilder("kkk","xxxx1",mapToRow(Person.class))

                .saveToCassandra();
        //        CassandraJavaUtil.javaFunctions(dataFrame.javaRDD()).writerBuilder("kkk","xxxx",CassandraJavaUtil.someColumns("name","b1"));

        dataFrame.groupBy("n1").count().orderBy(col("count").desc()).show();

        Map<String, String> options1 = new HashMap<String, String>();
        options1.put("url", "jdbc:postgresql://192.168.100.105:5432/ala?user=zulk&loglevel=2");
        options1.put("dbtable", "test1");
        options1.put("driver", "org.postgresql.Driver");
        options1.put("partitionColumn", "id");
        options1.put("lowerBound", "0");
        options1.put("upperBound", "1000");
        options1.put("numPartitions","2");

        DataFrame dataFrame1 = sqlContext.read().format("jdbc").options(options1).load();

        dataFrame1.groupBy("id").count().orderBy(col("count").desc()).show();

        dataFrame.printSchema();
        dataFrame1.printSchema();

        dataFrame.join(cassandraDF,dataFrame.col("n1").equalTo(cassandraDF.col("dd"))).show();

        System.out.println("---------------------------------------------------------------------");
        dataFrame.registerTempTable("d1");
        dataFrame1.registerTempTable("d2");
        DataFrame  joined  = sqlContext.sql("select d1.name name1,d2.name name2,d1.n1 from d1 join d2 on d1.n1 = d2.id ");
        Dataset<J> dataset = joined.as(Encoders.bean(J.class));

//        dataset.foreach(f -> {
//            System.out.println(f.getName1());
//        });


        joined.registerTempTable("joined");

        DataFrame cache = joined.sqlContext().sql("select name1,name2,n1 from joined where name2 like 'ee%'");
        cache.registerTempTable("cc");
        sqlContext.cacheTable("cc");


        long count = cache.count();
        cache.filter(col("name1").startsWith("8ec")).show(20);

        System.out.println(count);

        cache.limit(10).write().mode(SaveMode.Append).jdbc("jdbc:postgresql://192.168.100.105:5432/ala?user=zulk&loglevel=2","ddddd",new Properties());

        cache.javaRDD().foreachPartition(f -> {
            Connection connection = JdbcUtils.createConnection("jdbc:postgresql://192.168.100.105:5432/ala?user=zulk&loglevel=2", new Properties());
            PreparedStatement ddddd = JdbcUtils.insertStatement(connection, "ddddd", cache.schema());
//            f.forEachRemaining(r -> r.);
        });



//        cache.fo
//        joined.sqlContext().sql("select count(*) from joined where name2 like 'ee%'").show();


//        DataFrame sql = sqlContext.sql("select joined.name1 from joined join brd_temp on joined.name1 = brd_temp.name");

//        sql.show();
//                .javaRDD()
//                .map(r -> new Data(r.getString(0), r.getString(1), r.getInt(2)))
//                .collectAsync();

//        int size = listJavaFutureAction.get().size();
//        System.out.println(size);

//        DataFrame join = dataFrame.join(dataFrame1, dataFrame.col("n1").equalTo(dataFrame1.col("id"))).select(dataFrame.col("name"),dataFrame.col("n1"));

//        join.first();
//        System.out.println(join.count());
//        join.write().format("json").mode(SaveMode.Append).save("/tmp/wynik.json");
//        join.filter(join.col("n1").geq(500)).orderBy(join.col("n1")).show();


//        dataFrame.registerTempTable("test");

//        DataFrame sql = sqlContext.sql("select * from test limit 9");
//        sql.show();
//        sql.agg(Functions)
//        System.out.println(sql.count());
//
//        long numAll = logData.count();
//        long numAs = logData.filter( s -> s.contains("f")).count();

//        long numBs = logData.filter( s -> s.contains("b")).count();


//        System.out.println(logData.getNumPartitions());
//        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs + " all: "+numAll);

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JdbcSparkTest jdbcSparkTest = new JdbcSparkTest();
        jdbcSparkTest.spark();
    }

}
