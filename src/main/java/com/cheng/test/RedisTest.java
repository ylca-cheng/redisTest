package com.cheng.test;

import com.cheng.domain.User;
import com.cheng.utils.RedisUtils;
import com.cheng.utils.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * Created by niecheng on 2018/3/28.
 */
public class RedisTest {
    public static void main(String[] args) {
        // 连接本地redis服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        // 查看服务是否运行
        System.out.println("服务正在运行："+jedis.ping());
        // jedis释放资源后还是可用的，但在多线程中使用连接池获取redis实例时，如果不释放资源，则其他线程拿不到当前实例
        // 只能由连接池分配新的实例，如果超过最大分配数，则会报错
        jedis.close();
        System.out.println("服务正在运行："+jedis.ping());

        Pipeline pipeline = jedis.pipelined();

        // 创建用户对象
        /*User user = new User();
        user.setId("U_1");
        user.setName("cheng");
        user.setAge(18);*/

        // 序列化对象
//        byte[] result = SerializeUtil.serializeObject(user);
//        System.out.println(result);
        // 放入redis缓存,过期时间为10秒
        //jedis.setex(user.getId().getBytes(), 200, result);
//        pipeline.set(user.getId().getBytes(), result);
//        pipeline.sync();
        long start1 = System.currentTimeMillis();
        String json = "{         \"encounterID\": \"WYBGH2018082301\",\t\t \"insuranceCode\": \"300\",\t    \"deptID\": \"\",\t\t\"roomNo\": \"\", \t\t\"bedNo\": \"1\", \t\t\"admissionDateExt\": \"20170715102245\",\t\t\"dischargeDateExt\": \"\", \t\t\"diagnosisCode\": \"J44.100\",  \t\t\"diagnosis\": \"慢性阻塞性肺病伴急性加重\",\t\t\"diagnoses\": [{\t\t\t\t\"code\": \"J44.100\", \t\t\t\t\"name\": \"慢性阻塞性肺病伴急性加重\"\t\t\t}], \t\t\"doctorID\": \"YS3493893\", \t\t\"hospitalCode\": \"123\",\t\t\"admissionDeptID\": \"3104\", \t\t\"adminssionDepartmentName\": \"妇科\", \t\t\"dischargeDeptID\": \"3104\", \t\t\"dischargeDepartmentName\": \"妇科\", \t\t\"encounterTypeExt\": \"100\",             \"ordersExt\": \t\t\t\t[{                    \"outId\": \"O000011111111111\", \t\t\t\t\t\"encounterId\": \"WYBGH2018082301\",                    \"doctorId\": \"013240\",                     \"prescriptionCode\": \"CFH01\",                     \"groupCode\": \"ZBH01\",                     \"isRepeat\": 0,                     \"orderType\": 7,                     \"detailTypeExt\": \"0\",                     \"medicareItemCode\": \"250403078\", \t\t\t\t\t\"medicareItemName\": \"13碳尿素呼气试验1\",                    \"hospitalItemCode\": \"250403078\",                     \"hospitalItemName\": \"13碳尿素呼气试验1\",                     \"hospitalDrugFormCode\": \"\",                     \"amount\": 1.0,                     \"price\": 5.0,                     \"cost\": 50.0,                     \"selfCost\": 4000,                    \"specification\": \"5ml:25mg\",                     \"specUnit\": \"瓶\",                     \"startDate\": \"2017-7-15\",                     \"stopDate\": \"2017-7-15\",                     \"issueOrderDeptId\": \"3104\",                     \"issueOrderDeptName\": \"妇科\"                },\t\t\t    {                    \"outId\": \"O000011111111112\", \t\t\t\t\t\"encounterId\": \"WYBGH2018082301\",                    \"doctorId\": \"013240\",                     \"prescriptionCode\": \"CFH01\",                     \"groupCode\": \"ZBH01\",                     \"isRepeat\": 0,                     \"orderType\": 7,                     \"detailTypeExt\": \"0\",                     \"medicareItemCode\": \"250403079\", \t\t\t\t\t\"medicareItemName\": \"13碳尿素呼气试验\",                    \"hospitalItemCode\": \"250403079\",                     \"hospitalItemName\": \"13碳尿素呼气试验\",                     \"hospitalDrugFormCode\": \"\", \t\t\t\t\t\"transationItemCode\": \"VI4987\",                    \"amount\": 10.0,                     \"price\": 5.0,                     \"cost\": 50.0,                     \"selfCost\": 4000,                    \"specification\": \"5ml:25mg\",                     \"specUnit\": \"瓶\",                     \"startDate\": \"2017-7-15\",                     \"stopDate\": \"2017-7-15\",                     \"issueOrderDeptId\": \"3104\",                     \"issueOrderDeptName\": \"妇科\"                },\t\t\t\t{                    \"outId\": \"O000011111111113\",                     \"doctorId\": \"013240\", \t\t\t\t\t\"encounterId\": \"WYBGH2018082301\",                    \"prescriptionCode\": \"CFH01\",                     \"groupCode\": \"ZBH01\",                     \"isRepeat\": 0,                     \"orderType\": 7,                     \"detailType\": \"0\",                     \"medicareItemCode\": \"250403080\", \t\t\t\t\t\"medicareItemName\": \"13碳尿素呼气试验3\",                    \"hospitalItemCode\": \"250403080\",                     \"hospitalItemName\": \"13碳尿素呼气试验3\",                     \"hospitalDrugFormCode\": \"\",                     \"amount\": 10.0,                     \"price\": 5.0,                     \"cost\": 50.0,                     \"selfCost\": 4000,                    \"specification\": \"5ml:25mg\",                     \"specUnit\": \"瓶\",                     \"startDate\": \"2017-7-15\",                     \"stopDate\": \"2017-7-15\",                     \"issueOrderDeptId\": \"3104\",                     \"issueOrderDeptName\": \"妇科\"                }],            \"pregnantFlag\": 99,             \"cost\": \"15000\"        }";
//        jedis.set("GHIDWYBGH2018082301",json);
        for(int i = 0; i< 500000; i++){
//            pipeline.set((i+"").getBytes(), json.getBytes());
            jedis.set(i+"",json);
        }
//        pipeline.sync();
        long end1 = System.currentTimeMillis();
        System.out.println("set without pipeline used [" + (end1 - start1) / 1000 + "] seconds ..");
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // 从redis缓存获取数据
        /*byte[] result1 = jedis.get(user.getId().getBytes());
        // 反序列化
        User cheng = (User)SerializeUtil.deserializeObject(result1);
        System.out.println(cheng);*/

        jedis.close();
    }
}
