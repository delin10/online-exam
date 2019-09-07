package nil.ed.onlineexam.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import nil.ed.onlineexam.AbstractServiceTest;
import nil.ed.onlineexam.service.ITestPaperService;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TestPaperServiceImplTest extends AbstractServiceTest {
    @Resource
    private ITestPaperService testPaperService;

    @Test
    public void generateTestPaperRandomly() {
        printAsJsonString(testPaperService.generateTestPaperRandomly(5,5));
    }

    @Test
    public void addTestPaper() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("testDuration", 60);
        jsonObject.put("startTime", Instant.now().toEpochMilli());
        jsonObject.put("endTime", Instant.now().toEpochMilli() + 100 * 60  *  1000);
        jsonObject.put("score", 100);
        jsonObject.put("cid", 10);
        jsonObject.put("name", "考试");

        List<JSONObject> options = IntStream.range(100, 110)
                .filter(i -> i%2 == 1)
                .mapToObj(this::toScoreQidPair)
                .collect(Collectors.toList());

        List<JSONObject> subjectives = IntStream.range(100, 110)
                .filter(i -> i%2 == 0)
                .mapToObj(this::toScoreQidPair)
                .collect(Collectors.toList());

        jsonObject.put("options", options);
        jsonObject.put("subjectives", subjectives);

        testPaperService.addTestPaper(jsonObject, 2);
    }

    @Test
    public void getTestPaper() {
    }

    @Test
    public void generateTestPaperRandomly1() {
    }

    private JSONObject toScoreQidPair(Integer qid){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qid", qid);
        jsonObject.put("score", 10);
        return jsonObject;
    }

    @Test
    public void joinTest() {
        testPaperService.joinTest(2,2);
    }
}