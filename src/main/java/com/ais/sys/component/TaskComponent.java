package com.ais.sys.component;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.models.OneNoteTask;
import com.ais.sys.models.Quote;
import com.ais.sys.models.SystemParametersModel;
import com.ais.sys.services.OneNoteTaskService;
import com.ais.sys.services.QuoteService;
import com.ais.sys.services.SystemParametersService;
import com.ais.sys.utils.JsonUtils;
import com.ais.sys.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Component("task")
public class TaskComponent {

    private static final Logger log = LoggerFactory.getLogger(TaskComponent.class);

    private static final String quoteUrl = "http://api.avatardata.cn/MingRenMingYan/LookUp";

    private static final String quoteParam = "key=%s&keyword=%s&page=%d&rows=%d";

    private static final int rows = 20;

    private static String KEY;

    @Resource
    QuoteService quoteService;

    @Resource
    SystemParametersService systemParametersService;

    @Resource
    OneNoteTaskService oneNoteTaskService;

    @Scheduled(cron = "12 12 */2 * * ?")  // 每两个小时执行一次
    public void quoteJob() throws ServiceException {
        log.info("quote job start");
        startQuoteTask();
        log.info("quote job end");
    }

    private void startQuoteTask() throws ServiceException {
        String key  = getApiKey();
        OneNoteTask task = getOneNoteTask();
        if(task == null) {
            log.info("【没有任务任务】");
            return ;
        }
        task.setStatus(2);
        oneNoteTaskService.update(task);
        doTask(1, key, task);
    }

    private void doTask(int page, String key, OneNoteTask task) {
        try {
            String keyEncode = URLEncoder.encode(task.getKeyWord(), "UTF-8");
            String res = sendGet(quoteUrl, String.format(quoteParam, key, keyEncode, page, rows));
            Map<String, Object> map = (Map<String, Object>) JsonUtils.json2Object(res, Map.class);
            int errorCode = (int) map.get("error_code");
            if(errorCode == 0) {
                int total = (Integer) map.get("total");
                List<Map<String, String>> resultList = (List<Map<String, String>>) map.get("result");
                resultList.forEach(item -> {
                    Quote quote = new Quote();
                    quote.setCreateBy("Task");
                    quote.setLastUpdateBy("Task");
                    quote.setAuthor(item.get("famous_name"));
                    quote.setQuote(item.get("famous_saying"));
                    quote.setKeyWord(task.getKeyWord());
                    try {
                        quoteService.insert(quote);
                    }catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
                if(page * rows < total) {
                    doTask(++ page, key, task);
                } else {
                    task.setStatus(3); // success
                    oneNoteTaskService.update(task);
                }
                return ;
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        task.setStatus(4); // fail
        oneNoteTaskService.update(task);
    }

    private OneNoteTask getOneNoteTask() {
        OneNoteTask task = new OneNoteTask();
        task.setStatus(1);
        return oneNoteTaskService.findOne(task);
    }

    private String getApiKey() throws ServiceException {
        if(KEY != null) return KEY;
        String key = "";
        SystemParametersModel model = new SystemParametersModel();
        model.setSegment("OneNote");
        model.setCode("API_TOKEN");
        List<SystemParametersModel> paramList = systemParametersService.searchList(model);
        if(CollectionUtils.isNotEmpty(paramList)) {
            key = paramList.get(0).getAttrib01();
            KEY = key;
        }
        return key;
    }

    public static String sendGet(String url, String param) throws UnsupportedEncodingException, IOException {
        return sendGet(url, param, null);
    }
    public static String sendGet(String url, String param, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        String result = "";
        BufferedReader in = null;
        String urlNameString = url + "?" + param;
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        //设置超时时间
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(15000);
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : map.keySet()) {
            System.out.println(key + "--->" + map.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应，设置utf8防止中文乱码
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if (in != null) {
            in.close();
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(URLEncoder.encode("朱自清", "UTF-8"));
        //http://api.avatardata.cn/MingRenMingYan/LookUp?key=39f04800aa4740939470b9dcafd90861&keyword=%E6%9C%B1%E8%87%AA%E6%B8%85&page=1&rows=20
    }

}