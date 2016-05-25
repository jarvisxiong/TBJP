package com.tbjp.profiler;

import java.util.Random;

/**
 * 测试
 *
 * @author: tubingbing
 * @Date: 16-5-25
 * @Time: 上午9:44
 */
public class Test {
    public static void main(String[] args) {
        Random r = new Random();
        for(int i=0;i<5000;i++){
        CallerInfo callerInfo =Profiler.start("com.tu.bb.AddTest",true,true);
            try {
                int j = r.nextInt(5000);
                Thread.sleep(10);
                if(j%1000==0){
                    throw  new Exception();
                }
            } catch (Exception e) {
                Profiler.error(callerInfo);
                //e.printStackTrace();
            }finally {
                Profiler.end(callerInfo);
            }
        }

    }
}
